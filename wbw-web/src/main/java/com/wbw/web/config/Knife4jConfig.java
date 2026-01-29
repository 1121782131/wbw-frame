package com.wbw.web.config;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Knife4j增强配置
 */
@Configuration
public class Knife4jConfig {
    
    /**
     * 全局OpenAPI自定义配置
     */
    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 添加全局参数
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) -> {
                    pathItem.readOperations().forEach(operation -> {
                        // 添加请求时间戳参数
                        Parameter timestampParam = new Parameter()
                                .in("header")
                                .name("Timestamp")
                                .description("请求时间戳")
                                .required(false)
                                .example(String.valueOf(System.currentTimeMillis()));
                        
                        // 添加签名参数
                        Parameter signParam = new Parameter()
                                .in("header")
                                .name("Signature")
                                .description("请求签名")
                                .required(false)
                                .example("");
                        
                        if (operation.getParameters() == null) {
                            operation.setParameters(new ArrayList<>());
                        }
                        operation.getParameters().add(timestampParam);
                        operation.getParameters().add(signParam);
                    });
                });
            }
        };
    }
}