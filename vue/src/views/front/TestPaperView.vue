<template>
  <div class="paper-container">
    <!-- 试卷头部信息 -->
    <div class="paper-header">
      <div class="header-content">
        <h1 class="paper-title">{{ data.testPaperData.name }}</h1>
        <div class="paper-info">
          <div class="info-item">
            <span class="info-label">课程名称</span>
            <span class="info-value">{{ data.testPaperData.courseName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">授课教师</span>
            <span class="info-value">{{ data.testPaperData.teacherName }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 试卷内容区域 -->
    <div class="paper-content">
      <div class="questions-container">
        <div v-for="(item, index) in data.testPaperData.questions" 
             :key="index" 
             class="question-card">
          <!-- 题目标题 -->
          <div class="question-header">
            <div class="question-type">
              <span class="type-tag">{{ getQuestionType(item.typeId) }}</span>
              <span class="score-tag">{{ item.typeScore }} 分</span>
            </div>
            <div class="question-title">{{ item.name }}</div>
          </div>

          <!-- 题目内容 -->
          <div class="question-body">
            <!-- 单选题 -->
            <div v-if="item.typeId === 1" class="options-group">
              <el-radio-group v-model="item.newAnswer" disabled class="apple-radio-group">
                <el-radio v-for="option in ['A', 'B', 'C', 'D']" 
                         :key="option" 
                         :value="option"
                         :class="{'correct-answer': item.answer === option}"
                         class="apple-radio">
                  {{ option }}. {{ item['option' + option] }}
                </el-radio>
              </el-radio-group>
            </div>

            <!-- 多选题 -->
            <div v-if="item.typeId === 2" class="options-group">
              <el-checkbox-group v-model="item.checkList" disabled class="apple-checkbox-group">
                <el-checkbox v-for="option in ['A', 'B', 'C', 'D']"
                            :key="option"
                            :value="option"
                            :class="{'correct-answer': item.answer.includes(option)}"
                            class="apple-checkbox">
                  {{ option }}. {{ item['option' + option] }}
                </el-checkbox>
              </el-checkbox-group>
            </div>

            <!-- 判断题 -->
            <div v-if="item.typeId === 3" class="options-group">
              <el-radio-group v-model="item.newAnswer" disabled class="apple-radio-group">
                <el-radio value="正确" :class="{'correct-answer': item.answer === '正确'}" class="apple-radio">
                  A. 正确
                </el-radio>
                <el-radio value="错误" :class="{'correct-answer': item.answer === '错误'}" class="apple-radio">
                  B. 错误
                </el-radio>
              </el-radio-group>
            </div>

            <!-- 填空题 -->
            <div v-if="item.typeId === 4" class="fill-blank">
              <el-input 
                disabled 
                v-model="item.newAnswer" 
                class="apple-input"
                placeholder="你的答案">
              </el-input>
            </div>

            <!-- 简答题 -->
            <div v-if="item.typeId === 5" class="essay">
              <div class="answer-section">
                <div class="answer-label">你的答案：</div>
                <el-input 
                  disabled 
                  type="textarea" 
                  :rows="4" 
                  v-model="item.newAnswer"
                  class="apple-textarea"
                  placeholder="你的答案">
                </el-input>
              </div>
            </div>

            <!-- 答案展示 -->
            <div class="answer-display">
              <div class="answer-header">
                <el-icon><InfoFilled /></el-icon>
                <span>标准答案</span>
              </div>
              <div class="answer-content" :class="{'answer-long': item.typeId === 5}">
                {{ item.answer }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import { InfoFilled } from '@element-plus/icons-vue'

const data = reactive({
  scoreId: router.currentRoute.value.query.id,
  testPaperData: {},
})

const getQuestionType = (typeId) => {
  const types = {
    1: '单选题',
    2: '多选题',
    3: '判断题',
    4: '填空题',
    5: '简答题'
  }
  return types[typeId] || '未知类型'
}

const loadTestPaper = () => {
  data.scoreId = router.currentRoute.value.query.id
  request.get('/score/selectById/' + data.scoreId).then(res => {
    if (res.code === '200') {
      data.testPaperData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadTestPaper()
</script>

<style scoped>
.paper-container {
  min-height: 100vh;
  background-color: #f5f5f7;
}

/* 试卷头部样式 */
.paper-header {
  background: linear-gradient(135deg, #ffffff 0%, #f5f5f7 100%);
  padding: 40px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
}

.paper-title {
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 16px 0;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.paper-info {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.info-item {
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
}

.info-label {
  color: #86868b;
  font-size: 14px;
}

.info-value {
  color: #1d1d1f;
  font-weight: 500;
  font-size: 14px;
}

/* 试卷内容区域样式 */
.paper-content {
  padding: 20px 0;
}

.questions-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.question-card {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
  margin-bottom: 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.question-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.08);
}

.question-header {
  padding: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
}

.question-type {
  margin-bottom: 8px;
  gap: 8px;
}

.type-tag, .score-tag {
  padding: 4px 10px;
  font-size: 12px;
}

.question-title {
  font-size: 15px;
  line-height: 1.4;
}

.question-body {
  padding: 16px;
}

/* 选项组样式 */
.options-group {
  margin-bottom: 16px;
}

.apple-radio-group,
.apple-checkbox-group {
  gap: 8px;
}

:deep(.el-radio),
:deep(.el-checkbox) {
  padding: 10px 14px;
  border-radius: 10px;
  font-size: 14px;
}

:deep(.el-radio:hover),
:deep(.el-checkbox:hover) {
  background: #ebebeb;
}

:deep(.el-radio.correct-answer),
:deep(.el-checkbox.correct-answer) {
  background: #e3f9e5;
}

/* 填空题样式 */
.fill-blank {
  margin-bottom: 16px;
}

:deep(.el-input__wrapper) {
  padding: 6px 12px;
}

/* 简答题样式 */
.essay {
  margin-bottom: 16px;
}

.answer-section {
  margin-bottom: 12px;
}

.answer-label {
  font-size: 14px;
  color: #86868b;
  margin-bottom: 8px;
}

:deep(.el-textarea__inner) {
  border-radius: 12px;
  padding: 12px 16px;
  background: #f5f5f7;
  resize: none;
}

/* 答案展示样式 */
.answer-display {
  padding: 12px;
  border-radius: 10px;
  margin-top: 12px;
}

.answer-header {
  margin-bottom: 8px;
  font-size: 13px;
}

.answer-content {
  font-size: 13px;
  line-height: 1.4;
}

.answer-long {
  white-space: pre-wrap;
}
</style>