package com.wbw.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 单个数据源配置属性
 */
@Data
public class DataSourceProperties {
    
    /**
     * 数据源名称
     */
    private String name;
    
    /**
     * 数据库URL
     */
    private String url;
    
    /**
     * 数据库用户名
     */
    private String username;
    
    /**
     * 数据库密码
     */
    private String password;
    
    /**
     * 数据库驱动类名
     */
    private String driverClassName;
    
    /**
     * 数据源类型
     */
    private Class<? extends DataSource> type;
    
    /**
     * 连接池配置
     */
    private PoolProperties pool;
    
    /**
     * 其他自定义属性
     */
    private Map<String, Object> hikari;
    
    /**
     * 连接池配置
     */
    @Data
    public static class PoolProperties {
        
        /**
         * 最小连接数
         */
        private int minimumIdle = 5;
        
        /**
         * 最大连接数
         */
        private int maximumPoolSize = 20;
        
        /**
         * 连接超时时间（毫秒）
         */
        private long connectionTimeout = 30000;
        
        /**
         * 连接最大空闲时间（毫秒）
         */
        private long idleTimeout = 600000;
        
        /**
         * 连接最大生命周期（毫秒）
         */
        private long maxLifetime = 1800000;
        
        /**
         * 连接测试语句
         */
        private String connectionTestQuery = "SELECT 1";
    }
}
