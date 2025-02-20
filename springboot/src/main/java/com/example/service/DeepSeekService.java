package com.example.service;

import com.example.config.DeepSeekConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.cache.annotation.Cacheable;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.exception.CustomException;

@Slf4j
@Service
public class DeepSeekService {

    @Resource
    private DeepSeekConfig deepSeekConfig;
    
    @Autowired
    private Bucket rateLimitBucket;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "aiGradingResults", key = "#questionName + '-' + #standardAnswer + '-' + #studentAnswer")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Map<String, Object> getGradingSuggestion(String questionName, String standardAnswer, String studentAnswer, Integer fullScore) {
        // 检查请求限制
        if (!rateLimitBucket.tryConsume(1)) {
            throw new CustomException("请求过于频繁，请稍后再试");
        }

        try {
            String systemPrompt = "你是一位经验丰富的教师，擅长评阅试卷和给出专业的评分建议。你需要根据题目要求和标准答案评估学生答案的正确性，并给出合理的分数和详细的评分说明。请确保返回的是合法的JSON格式。";
            
            String userPrompt = String.format(
                "请评阅以下试题答案：\n\n" +
                "题目：%s\n\n" +
                "标准答案：\n%s\n\n" +
                "学生答案：\n%s\n\n" +
                "满分分值：%d分\n\n" +
                "请严格按照以下JSON格式返回评分结果（确保是合法的JSON格式）：\n" +
                "{\n" +
                "  \"suggestedScore\": [建议分数，必须是数字，范围0-%d],\n" +
                "  \"feedback\": \"[整体评价，说明得分理由]\",\n" +
                "  \"scoringDetails\": \"[具体的评分细则，列出得分点和扣分点，用换行符分隔]\"\n" +
                "}\n\n" +
                "注意：\n" +
                "1. 分数必须是数字，不要带引号\n" +
                "2. 分数范围是0到%d\n" +
                "3. feedback 和 scoringDetails 必须是字符串格式，可以包含换行符\n" +
                "4. 请确保返回的是合法的JSON格式",
                questionName, standardAnswer, studentAnswer, fullScore, fullScore, fullScore);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1-distill-qwen-7b-250120");
            requestBody.put("messages", new ArrayList<Map<String, String>>() {{
                add(new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", systemPrompt);
                }});
                add(new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", userPrompt);
                }});
            }});

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + deepSeekConfig.getApiKey());
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("发送AI评分请求: {}", questionName);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                deepSeekConfig.getApiUrl() + "/chat/completions",
                entity,
                Map.class
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new CustomException("API调用失败: " + responseEntity.getStatusCode());
            }

            Map<String, Object> response = responseEntity.getBody();
            if (response == null || !response.containsKey("choices")) {
                throw new CustomException("API响应格式错误");
            }

            List<?> choices = (List<?>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new CustomException("API响应中没有选项");
            }

            Map<String, Object> choice = (Map<String, Object>) choices.get(0);
            Map<String, Object> messageObj = (Map<String, Object>) choice.get("message");
            String content = (String) messageObj.get("content");

            log.info("收到AI响应: {}", content);

            try {
                // 清理响应内容中的 Markdown 代码块标记
                content = content.replaceAll("```json\\s*", "")
                               .replaceAll("```\\s*", "")
                               .trim();
                
                Map<String, Object> result = objectMapper.readValue(content, Map.class);
                
                // 验证必要字段
                if (!result.containsKey("suggestedScore") || 
                    !result.containsKey("feedback") || 
                    !result.containsKey("scoringDetails")) {
                    throw new CustomException("AI返回的JSON格式不完整");
                }

                // 确保 suggestedScore 是整数
                Object score = result.get("suggestedScore");
                if (score instanceof String) {
                    result.put("suggestedScore", Integer.parseInt((String) score));
                } else if (score instanceof Double) {
                    result.put("suggestedScore", ((Double) score).intValue());
                }

                // 处理 scoringDetails，如果是数组则转换为字符串
                Object scoringDetails = result.get("scoringDetails");
                if (scoringDetails instanceof List) {
                    List<?> detailsList = (List<?>) scoringDetails;
                    String detailsStr = String.join("\n", detailsList.stream()
                        .map(Object::toString)
                        .toList());
                    result.put("scoringDetails", detailsStr);
                }

                // 验证分数范围
                int suggestedScore = (int) result.get("suggestedScore");
                if (suggestedScore < 0 || suggestedScore > fullScore) {
                    throw new CustomException("AI建议分数超出范围: " + suggestedScore);
                }

                return result;
            } catch (Exception e) {
                log.error("解析AI响应失败: {}", e.getMessage());
                throw new CustomException("解析AI响应失败: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("AI评分服务异常: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "AI评分服务暂时不可用: " + e.getMessage());
            return errorResponse;
        }
    }
} 