package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许所有域名访问
        corsConfiguration.addAllowedOriginPattern("*");
        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许所有请求方法
        corsConfiguration.addAllowedMethod("*");
        // 允许携带凭证信息（cookies等）
        corsConfiguration.setAllowCredentials(true);
        // 暴露响应头信息
        corsConfiguration.addExposedHeader("*");
        // 设置跨域请求的有效期，单位秒
        corsConfiguration.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}