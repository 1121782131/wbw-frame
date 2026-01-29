package com.wbw.mybatis.autoconfigure;

import com.wbw.mybatis.config.DynamicDataSourceConfig;
import com.wbw.mybatis.config.MyBatisPlusConfig;
import com.wbw.mybatis.config.MyBatisProperties;
import com.wbw.mybatis.config.TransactionConfig;
import com.wbw.mybatis.config.TransactionProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * MyBatis Plus 自动配置类
 */
@AutoConfiguration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.core.MybatisConfiguration")
@EnableConfigurationProperties({MyBatisProperties.class, TransactionProperties.class})
@ComponentScan("com.wbw.mybatis")
@MapperScan(basePackages = "${wbw.mybatis.mapper-scan:com.wbw.**.mapper}")
@Import({
    MyBatisPlusConfig.class,
    TransactionConfig.class,  // 导入事务配置
    DynamicDataSourceConfig.class  // 导入动态数据源配置
})
public class MyBatisAutoConfiguration {
    
    /**
     * 注册配置属性到Spring环境
     */
    public MyBatisAutoConfiguration(MyBatisProperties myBatisProperties, 
                                   TransactionProperties transactionProperties) {
        // 可以根据需要在这里初始化一些配置
    }
}