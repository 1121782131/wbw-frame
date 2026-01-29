package com.wbw.web.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Web自动配置类
 */
@AutoConfiguration
@ComponentScan("com.wbw.web")
@Import({
    com.wbw.web.config.WebConfig.class,
    com.wbw.web.config.OpenApiConfig.class,
    com.wbw.web.config.ValidatorConfig.class
})
public class WebAutoConfiguration {
    
    /**
     * 配置扫描包路径
     */
    public static final String BASE_PACKAGE = "com.wbw.web";
}