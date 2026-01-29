package com.wbw.nacos.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nacos配置中心配置属性类
 * 用于绑定application.yml中的nacos.config配置
 */
@Data
@ConfigurationProperties(prefix = "nacos.config")
public class NacosConfigProperties {
    
    /**
     * Nacos服务地址
     */
    private String serverAddr = "localhost:8848";
    
    /**
     * 命名空间
     */
    private String namespace = "";
    
    /**
     * 分组
     */
    private String group = "DEFAULT_GROUP";
    
    /**
     * 数据ID
     */
    private String dataId;
    
    /**
     * 配置文件类型
     */
    private String fileExtension = "yml";
    
    /**
     * 最大重试次数
     */
    private int maxRetry = 3;
    
    /**
     * 重试间隔时间（毫秒）
     */
    private int retryInterval = 1000;
    
    /**
     * 是否开启自动刷新
     */
    private boolean autoRefresh = true;
    
    /**
     * 配置获取超时时间（毫秒）
     */
    private int timeout = 3000;
    
    /**
     * 是否开启加密
     */
    private boolean encryptEnabled = false;
    
    /**
     * 加密算法
     */
    private String encryptAlgorithm = "AES";
}
