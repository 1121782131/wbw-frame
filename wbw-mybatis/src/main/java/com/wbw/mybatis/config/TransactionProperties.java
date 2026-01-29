package com.wbw.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 事务配置属性
 */
@Data
@ConfigurationProperties(prefix = "wbw.transaction")
public class TransactionProperties {
    
    /**
     * 是否启用事务管理
     * 默认：true
     */
    private boolean enabled = true;
    
    /**
     * 默认超时时间（秒）
     * 默认：30秒
     */
    private int timeout = 30;
    
    /**
     * 只读事务超时时间（秒）
     * 默认：10秒
     */
    private int readOnlyTimeout = 10;
    
    /**
     * 是否启用全局事务拦截器
     * 默认：true
     */
    private boolean globalInterceptor = true;
    
    /**
     * 是否启用事务监控切面
     * 默认：true
     */
    private boolean enableMonitor = true;
    
    /**
     * 事务隔离级别
     * 可选值：DEFAULT, READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
     * 默认：DEFAULT（使用数据库默认隔离级别）
     */
    private String isolationLevel = "DEFAULT";
    
    /**
     * 事务传播行为
     * 可选值：REQUIRED, SUPPORTS, MANDATORY, REQUIRES_NEW, NOT_SUPPORTED, NEVER, NESTED
     * 默认：REQUIRED
     */
    private String propagationBehavior = "REQUIRED";
}