package com.wbw.nacos.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nacos服务发现配置属性类
 * 用于绑定application.yml中的nacos.discovery配置
 */
@Data
@ConfigurationProperties(prefix = "nacos.discovery")
public class NacosDiscoveryProperties {
    
    /**
     * Nacos服务地址
     */
    private String serverAddr = "localhost:8848";
    
    /**
     * 命名空间
     */
    private String namespace = "";
    
    /**
     * 服务名
     */
    private String service;
    
    /**
     * 分组
     */
    private String group = "DEFAULT_GROUP";
    
    /**
     * 集群名
     */
    private String clusterName = "DEFAULT";
    
    /**
     * 权重
     */
    private float weight = 1.0f;
    
    /**
     * 是否开启健康检查
     */
    private boolean enableHealthCheck = true;
    
    /**
     * 健康检查间隔时间（毫秒）
     */
    private int healthCheckInterval = 5000;
    
    /**
     * 元数据
     */
    private Metadata metadata = new Metadata();
    
    /**
     * 元数据类
     */
    @Data
    public static class Metadata {
        private String version = "1.0.0";
        private String environment = "dev";
        private String region = "default";
    }
}
