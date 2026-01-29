package com.wbw.nacos.health;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Nacos健康检查指标类
 * 用于集成Spring Boot Actuator的健康检查功能
 */
@Slf4j
@Component("nacos")
public class NacosHealthIndicator extends AbstractHealthIndicator implements HealthIndicator {
    
    private final NacosHealthConfig nacosHealthConfig;
    private final NacosServiceManager nacosServiceManager;
    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    private final NacosConfigManager nacosConfigManager;
    
    public NacosHealthIndicator(NacosHealthConfig nacosHealthConfig, NacosServiceManager nacosServiceManager, NacosDiscoveryProperties nacosDiscoveryProperties, NacosConfigManager nacosConfigManager) {
        this.nacosHealthConfig = nacosHealthConfig;
        this.nacosServiceManager = nacosServiceManager;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.nacosConfigManager = nacosConfigManager;
    }
    
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        if (!nacosHealthConfig.isEnabled()) {
            builder.up().withDetail("enabled", false).withDetail("message", "Nacos health check is disabled");
            return;
        }
        
        boolean discoveryHealthy = true;
        boolean configHealthy = true;
        
        // 检查服务发现
        if (nacosHealthConfig.isCheckDiscovery()) {
            discoveryHealthy = checkDiscoveryHealth();
        }
        
        // 检查配置中心
        if (nacosHealthConfig.isCheckConfig()) {
            configHealthy = checkConfigHealth();
        }
        
        if (discoveryHealthy && configHealthy) {
            builder.up()
                .withDetail("discovery", "healthy")
                .withDetail("config", "healthy")
                .withDetail("serverAddr", nacosDiscoveryProperties.getServerAddr());
        } else {
            builder.down()
                .withDetail("discovery", discoveryHealthy ? "healthy" : "unhealthy")
                .withDetail("config", configHealthy ? "healthy" : "unhealthy")
                .withDetail("serverAddr", nacosDiscoveryProperties.getServerAddr());
        }
    }
    
    /**
     * 检查服务发现健康状态
     * @return 是否健康
     */
    private boolean checkDiscoveryHealth() {
        try {
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            // 尝试获取服务列表，验证连接是否正常
            namingService.getServicesOfServer(1, 10);
            return true;
        } catch (NacosException e) {
            log.warn("Nacos discovery health check failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error in Nacos discovery health check: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查配置中心健康状态
     * @return 是否健康
     */
    private boolean checkConfigHealth() {
        try {
            // 尝试获取配置服务实例，验证连接是否正常
            nacosConfigManager.getConfigService();
            return true;
        } catch (Exception e) {
            log.warn("Nacos config health check failed: {}", e.getMessage());
            return false;
        }
    }
}
