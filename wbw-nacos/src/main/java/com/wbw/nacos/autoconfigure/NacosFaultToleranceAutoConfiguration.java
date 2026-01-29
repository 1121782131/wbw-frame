package com.wbw.nacos.autoconfigure;

import com.wbw.nacos.config.NacosFaultToleranceConfig;
import com.wbw.nacos.service.NacosFaultToleranceService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.springboot3.ratelimiter.autoconfigure.RateLimiterAutoConfiguration;
import io.github.resilience4j.springboot3.retry.autoconfigure.RetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Nacos容错处理自动配置类
 * 用于自动配置Resilience4j相关的Bean和Nacos容错处理服务
 */
@AutoConfiguration(after = {
        CircuitBreakerAutoConfiguration.class,
        RateLimiterAutoConfiguration.class,
        RetryAutoConfiguration.class
})
@EnableConfigurationProperties(NacosFaultToleranceConfig.class)
@ConditionalOnClass(name = "io.github.resilience4j.circuitbreaker.CircuitBreaker")
@ConditionalOnProperty(name = "nacos.fault-tolerance.enabled", havingValue = "true", matchIfMissing = true)
public class NacosFaultToleranceAutoConfiguration {
    
    /**
     * 创建NacosFaultToleranceService实例
     * @param faultToleranceConfig 容错处理配置
     * @param circuitBreakerRegistry 熔断注册中心
     * @param rateLimiterRegistry 限流注册中心
     * @param retryRegistry 重试注册中心
     * @return NacosFaultToleranceService实例
     */
    @Bean
    @ConditionalOnMissingBean
    public NacosFaultToleranceService nacosFaultToleranceService(
            NacosFaultToleranceConfig faultToleranceConfig,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RateLimiterRegistry rateLimiterRegistry,
            RetryRegistry retryRegistry) {
        return new NacosFaultToleranceService(faultToleranceConfig, circuitBreakerRegistry, rateLimiterRegistry, retryRegistry);
    }
}
