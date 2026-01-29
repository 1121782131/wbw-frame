package com.wbw.nacos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nacos容错处理配置类
 * 用于配置容错处理相关的参数
 */
@Data
@ConfigurationProperties(prefix = "nacos.fault-tolerance")
public class NacosFaultToleranceConfig {
    
    /**
     * 是否启用容错处理
     */
    private boolean enabled = true;
    
    /**
     * 服务降级配置
     */
    private Degrade degrade = new Degrade();
    
    /**
     * 服务熔断配置
     */
    private CircuitBreaker circuitBreaker = new CircuitBreaker();
    
    /**
     * 服务限流配置
     */
    private RateLimit rateLimit = new RateLimit();
    
    /**
     * 服务降级配置类
     */
    @Data
    public static class Degrade {
        /**
         * 是否启用服务降级
         */
        private boolean enabled = true;
        
        /**
         * 降级阈值（失败率百分比）
         */
        private double failureThreshold = 50.0;
        
        /**
         * 统计时间窗口（毫秒）
         */
        private long statWindowMs = 10000;
        
        /**
         * 最小请求数
         */
        private int minRequestAmount = 5;
    }
    
    /**
     * 服务熔断配置类
     */
    @Data
    public static class CircuitBreaker {
        /**
         * 是否启用服务熔断
         */
        private boolean enabled = true;
        
        /**
         * 熔断阈值（失败率百分比）
         */
        private double failureThreshold = 50.0;
        
        /**
         * 统计时间窗口（毫秒）
         */
        private long statWindowMs = 10000;
        
        /**
         * 最小请求数
         */
        private int minRequestAmount = 5;
        
        /**
         * 熔断时长（毫秒）
         */
        private long sleepWindowMs = 10000;
    }
    
    /**
     * 服务限流配置类
     */
    @Data
    public static class RateLimit {
        /**
         * 是否启用服务限流
         */
        private boolean enabled = true;
        
        /**
         * 限流阈值（QPS）
         */
        private int limit = 100;
        
        /**
         * 统计时间窗口（毫秒）
         */
        private long statWindowMs = 1000;
        
        /**
         * 限流策略（0: 直接拒绝, 1: 预热, 2: 排队等待）
         */
        private int strategy = 0;
        
        /**
         * 预热时间（毫秒）
         */
        private long warmUpPeriodSec = 10;
        
        /**
         * 排队等待超时时间（毫秒）
         */
        private long timeoutMs = 500;
    }
}
