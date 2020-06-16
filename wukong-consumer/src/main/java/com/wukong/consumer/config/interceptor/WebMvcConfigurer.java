package com.wukong.consumer.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //'/*'匹配一个请求
        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**/*");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}