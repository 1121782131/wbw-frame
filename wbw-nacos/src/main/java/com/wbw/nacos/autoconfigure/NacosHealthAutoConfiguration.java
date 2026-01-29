package com.wbw.nacos.autoconfigure;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.wbw.nacos.health.NacosHealthConfig;
import com.wbw.nacos.health.NacosHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Nacos健康检查自动配置类
 * 用于自动配置Nacos健康检查相关的Bean
 */
@AutoConfiguration
@EnableConfigurationProperties(NacosHealthConfig.class)
@ConditionalOnClass(name = "org.springframework.boot.actuate.health.HealthIndicator")
@ConditionalOnProperty(name = "nacos.health.enabled", havingValue = "true", matchIfMissing = true)
public class NacosHealthAutoConfiguration {
    
    /**
     * 创建NacosHealthIndicator实例
     * @param nacosHealthConfig Nacos健康检查配置
     * @param nacosServiceManager Nacos服务管理器
     * @param nacosDiscoveryProperties Nacos发现配置
     * @param nacosConfigManager Nacos配置管理器
     * @return NacosHealthIndicator实例
     */
    @Bean
    @ConditionalOnMissingBean
    public NacosHealthIndicator nacosHealthIndicator(
            NacosHealthConfig nacosHealthConfig,
            NacosServiceManager nacosServiceManager,
            NacosDiscoveryProperties nacosDiscoveryProperties,
            NacosConfigManager nacosConfigManager) {
        return new NacosHealthIndicator(nacosHealthConfig, nacosServiceManager, nacosDiscoveryProperties, nacosConfigManager);
    }
}
