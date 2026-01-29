package com.wbw.nacos.autoconfigure;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.wbw.nacos.service.NacosDiscoveryService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Nacos服务发现自动配置类
 * 用于自动配置Nacos服务发现相关的Bean
 */
@AutoConfiguration
@EnableConfigurationProperties(NacosDiscoveryProperties.class)
@ConditionalOnProperty(name = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
public class NacosDiscoveryAutoConfiguration {
    
    /**
     * 创建NacosDiscoveryService实例
     * @param nacosServiceManager Nacos服务管理器
     * @param nacosDiscoveryProperties Nacos发现配置
     * @return NacosDiscoveryService实例
     */
    @Bean
    @ConditionalOnMissingBean
    public NacosDiscoveryService nacosDiscoveryService(
            NacosServiceManager nacosServiceManager,
            NacosDiscoveryProperties nacosDiscoveryProperties) {
        return new NacosDiscoveryService(nacosServiceManager, nacosDiscoveryProperties);
    }
}
