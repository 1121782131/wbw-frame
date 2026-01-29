package com.wbw.nacos.autoconfigure;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.wbw.nacos.service.NacosConfigService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Nacos配置中心自动配置类
 * 用于自动配置Nacos配置中心相关的Bean
 */
@AutoConfiguration
@EnableConfigurationProperties(NacosConfigProperties.class)
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", havingValue = "true", matchIfMissing = true)
public class NacosConfigAutoConfiguration {
    
    /**
     * 创建NacosConfigService实例
     * @param nacosConfigManager Nacos配置管理器
     * @param nacosConfigProperties Nacos配置属性
     * @return NacosConfigService实例
     */
    @Bean
    @ConditionalOnMissingBean
    public NacosConfigService nacosConfigService(
            NacosConfigManager nacosConfigManager,
            NacosConfigProperties nacosConfigProperties) {
        return new NacosConfigService(nacosConfigManager, nacosConfigProperties);
    }
}
