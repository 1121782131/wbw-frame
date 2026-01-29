package com.wbw.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "wbw.redis")
public class RedisProperties {
    
    /**
     * Redis服务器地址，默认为localhost
     */
    private String host = "localhost";
    
    /**
     * Redis服务器端口，默认为6379
     */
    private int port = 6379;
    
    /**
     * 数据库索引，默认为0
     */
    private int database = 0;
    
    /**
     * 连接超时时间，默认2000ms
     */
    private Duration timeout = Duration.ofMillis(2000);
    
    /**
     * 密码，默认无密码
     */
    private String password;
    
    /**
     * 连接池配置
     */
    private Pool pool = new Pool();
    
    /**
     * 连接池配置类
     */
    @Data
    public static class Pool {
        /**
         * 连接池最大连接数，默认8
         */
        private int maxActive = 8;
        
        /**
         * 连接池最大空闲连接数，默认8
         */
        private int maxIdle = 8;
        
        /**
         * 连接池最小空闲连接数，默认0
         */
        private int minIdle = 0;
        
        /**
         * 获取连接时的最大等待时间，默认-1ms（无限等待）
         */
        private Duration maxWait = Duration.ofMillis(-1);
    }
    
    /**
     * 集群配置
     */
    private Cluster cluster = new Cluster();
    
    /**
     * 哨兵配置
     */
    private Sentinel sentinel = new Sentinel();
    
    /**
     * 是否启用SSL
     */
    private boolean ssl = false;
    
    @Data
    public static class Cluster {
        /**
         * 集群节点，格式：host:port,host:port
         */
        private String nodes;
        
        /**
         * 最大重定向次数
         */
        private Integer maxRedirects;
    }
    
    @Data
    public static class Sentinel {
        /**
         * 主节点名称
         */
        private String master;
        
        /**
         * 哨兵节点，格式：host:port,host:port
         */
        private String nodes;
    }
    
    /**
     * 是否启用缓存前缀
     */
    private boolean enableKeyPrefix = true;
    
    /**
     * 缓存前缀
     */
    private String keyPrefix = "wbw:";
    
    /**
     * 默认过期时间（秒），默认24小时
     */
    private long defaultExpire = 24 * 60 * 60;
    
    /**
     * 是否启用统计
     */
    private boolean enableStatistics = false;
    
    /**
     * 是否启用缓存，默认开启
     */
    private boolean cacheEnabled = true;
}