package com.wbw.nacos.health;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nacos健康检查配置类
 * 用于配置健康检查相关的参数
 */
@Data
@ConfigurationProperties(prefix = "nacos.health")
public class NacosHealthConfig {
    
    /**
     * 是否启用Nacos健康检查
     */
    private boolean enabled = true;
    
    /**
     * 健康检查超时时间（毫秒）
     */
    private int timeout = 3000;
    
    /**
     * 健康检查间隔时间（毫秒）
     */
    private int interval = 5000;
    
    /**
     * 连续失败次数阈值
     */
    private int failureThreshold = 3;
    
    /**
     * 连续成功次数阈值
     */
    private int successThreshold = 1;
    
    /**
     * 是否检查服务发现
     */
    private boolean checkDiscovery = true;
    
    /**
     * 是否检查配置中心
     */
    private boolean checkConfig = true;
}
