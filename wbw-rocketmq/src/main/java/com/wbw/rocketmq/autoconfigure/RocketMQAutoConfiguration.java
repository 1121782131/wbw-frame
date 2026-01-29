package com.wbw.rocketmq.autoconfigure;

import com.wbw.rocketmq.config.RocketMQProperties;
import com.wbw.rocketmq.exception.RocketMQExceptionHandler;
import com.wbw.rocketmq.producer.RocketMQProducerService;
import com.wbw.rocketmq.consumer.RocketMQConsumerService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ自动配置类
 * 实现Spring Boot自动配置功能
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQAutoConfiguration {

    /**
     * 配置RocketMQ异常处理器
     * @return RocketMQ异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public RocketMQExceptionHandler rocketMQExceptionHandler() {
        return new RocketMQExceptionHandler();
    }

    /**
     * 配置RocketMQ生产者服务
     * @param rocketMQTemplate RocketMQ模板
     * @param rocketMQProperties RocketMQ配置属性
     * @return RocketMQ生产者服务
     */
    @Bean
    @ConditionalOnMissingBean
    public RocketMQProducerService rocketMQProducerService(
            RocketMQTemplate rocketMQTemplate,
            RocketMQProperties rocketMQProperties) {
        return new RocketMQProducerService(rocketMQTemplate, rocketMQProperties);
    }

    /**
     * 配置RocketMQ消费者服务
     * @param rocketMQProperties RocketMQ配置属性
     * @return RocketMQ消费者服务
     */
    @Bean
    @ConditionalOnMissingBean
    public RocketMQConsumerService rocketMQConsumerService(
            RocketMQProperties rocketMQProperties) {
        return new RocketMQConsumerService(rocketMQProperties);
    }
}
