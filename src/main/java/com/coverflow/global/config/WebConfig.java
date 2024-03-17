package com.coverflow.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 uri에 대해 특정 도메인은 접근을 허용한다.
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://15.165.1.48", "http://15.165.1.48:3001", "http://172.17.0.3:3001")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .exposedHeaders("Authorization", "Authorization-refresh", HttpHeaders.LOCATION)
                .allowCredentials(true);
    }
}
