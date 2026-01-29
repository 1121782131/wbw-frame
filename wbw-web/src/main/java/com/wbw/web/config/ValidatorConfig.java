package com.wbw.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 验证器配置
 */
@Configuration
public class ValidatorConfig {
    
    /**
     * 方法参数验证处理器
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        // 快速失败模式
        processor.setProxyTargetClass(true);
        return processor;
    }
}