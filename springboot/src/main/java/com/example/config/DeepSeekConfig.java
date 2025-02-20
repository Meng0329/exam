package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class DeepSeekConfig {
    
    @Value("${deepseek.api.key}")
    private String apiKey;
    
    @Value("${deepseek.api.url:http://localhost:11434}")
    private String apiUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }
} 