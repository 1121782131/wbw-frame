package com.wbw.dubbo.transaction;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 测试配置类
 * 用于配置测试所需的Spring Bean
 */
@Configuration
@ComponentScan(basePackages = "com.wbw.dubbo.transaction")
public class TestConfig {

    // 这里可以添加测试所需的配置和Bean定义
    // 例如：数据源配置、事务管理器配置等
}
