package com.wbw.nacos.service;

import com.wbw.nacos.config.NacosFaultToleranceConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Nacos容错处理服务类
 * 提供服务降级、熔断和限流等容错处理功能
 */
@Slf4j
@Service
public class NacosFaultToleranceService {
    
    private final NacosFaultToleranceConfig faultToleranceConfig;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RateLimiterRegistry rateLimiterRegistry;
    private final RetryRegistry retryRegistry;
    
    private final Map<String, CircuitBreaker> circuitBreakers = new HashMap<>();
    private final Map<String, RateLimiter> rateLimiters = new HashMap<>();
    private final Map<String, Retry> retries = new HashMap<>();
    
    public NacosFaultToleranceService(NacosFaultToleranceConfig faultToleranceConfig, CircuitBreakerRegistry circuitBreakerRegistry, RateLimiterRegistry rateLimiterRegistry, RetryRegistry retryRegistry) {
        this.faultToleranceConfig = faultToleranceConfig;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.rateLimiterRegistry = rateLimiterRegistry;
        this.retryRegistry = retryRegistry;
    }
    
    /**
     * 获取或创建服务熔断实例
     * @param serviceName 服务名
     * @return 服务熔断实例
     */
    public CircuitBreaker getCircuitBreaker(String serviceName) {
        return circuitBreakers.computeIfAbsent(serviceName, name -> {
            CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                    .failureRateThreshold((float) faultToleranceConfig.getCircuitBreaker().getFailureThreshold())
                    .waitDurationInOpenState(Duration.ofMillis(faultToleranceConfig.getCircuitBreaker().getSleepWindowMs()))
                    .slidingWindowSize(100)
                    .minimumNumberOfCalls(faultToleranceConfig.getCircuitBreaker().getMinRequestAmount())
                    .build();
            return circuitBreakerRegistry.circuitBreaker(name, config);
        });
    }
    
    /**
     * 获取或创建服务限流实例
     * @param serviceName 服务名
     * @return 服务限流实例
     */
    public RateLimiter getRateLimiter(String serviceName) {
        return rateLimiters.computeIfAbsent(serviceName, name -> {
            RateLimiterConfig config = RateLimiterConfig.custom()
                    .limitForPeriod(faultToleranceConfig.getRateLimit().getLimit())
                    .limitRefreshPeriod(Duration.ofMillis(faultToleranceConfig.getRateLimit().getStatWindowMs()))
                    .timeoutDuration(Duration.ofMillis(faultToleranceConfig.getRateLimit().getTimeoutMs()))
                    .build();
            return rateLimiterRegistry.rateLimiter(name, config);
        });
    }
    
    /**
     * 获取或创建重试实例
     * @param serviceName 服务名
     * @return 重试实例
     */
    public Retry getRetry(String serviceName) {
        return retries.computeIfAbsent(serviceName, name -> {
            RetryConfig config = RetryConfig.custom()
                    .maxAttempts(3)
                    .waitDuration(Duration.ofMillis(100))
                    .build();
            return retryRegistry.retry(name, config);
        });
    }
    
    /**
     * 执行带容错处理的操作
     * @param serviceName 服务名
     * @param supplier 操作供应商
     * @param fallback 降级函数
     * @param <T> 返回类型
     * @return 操作结果
     */
    public <T> T executeWithFaultTolerance(String serviceName, Supplier<T> supplier, Supplier<T> fallback) {
        if (!faultToleranceConfig.isEnabled()) {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.warn("Service {} execution failed, no fault tolerance enabled: {}", serviceName, e.getMessage());
                return fallback.get();
            }
        }
        
        try {
            CircuitBreaker circuitBreaker = getCircuitBreaker(serviceName);
            RateLimiter rateLimiter = getRateLimiter(serviceName);
            Retry retry = getRetry(serviceName);
            
            // 组合使用容错组件
            return circuitBreaker.executeSupplier(() -> 
                rateLimiter.executeSupplier(() -> 
                    retry.executeSupplier(() -> {
                        try {
                            return supplier.get();
                        } catch (Exception e) {
                            log.warn("Service {} execution failed, using fallback: {}", serviceName, e.getMessage());
                            return fallback.get();
                        }
                    })
                )
            );
        } catch (Exception e) {
            log.warn("Service {} execution failed, using fallback: {}", serviceName, e.getMessage());
            return fallback.get();
        }
    }
    
    /**
     * 执行带容错处理的操作（无返回值）
     * @param serviceName 服务名
     * @param runnable 操作
     * @param fallback 降级操作
     */
    public void executeWithFaultTolerance(String serviceName, Runnable runnable, Runnable fallback) {
        executeWithFaultTolerance(serviceName, 
                () -> {
                    runnable.run();
                    return null;
                },
                () -> {
                    fallback.run();
                    return null;
                });
    }
    
    /**
     * 重置所有容错实例
     */
    public void resetAll() {
        circuitBreakers.clear();
        rateLimiters.clear();
        retries.clear();
        log.info("All fault tolerance instances reset");
    }
    
    /**
     * 重置指定服务的容错实例
     * @param serviceName 服务名
     */
    public void reset(String serviceName) {
        circuitBreakers.remove(serviceName);
        rateLimiters.remove(serviceName);
        retries.remove(serviceName);
        log.info("Fault tolerance instances reset for service: {}", serviceName);
    }
}
