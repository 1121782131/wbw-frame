package com.wbw.dubbo.autoconfigure;

import com.wbw.dubbo.config.DubboProperties;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dubbo自动配置类
 */
@Configuration
@EnableDubbo
@EnableConfigurationProperties(DubboProperties.class)
public class DubboAutoConfiguration {

    @Autowired
    private DubboProperties dubboProperties;

    /**
     * 配置Dubbo应用
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboProperties.getApplication().getName());
        applicationConfig.setModule(dubboProperties.getApplication().getModule());
        applicationConfig.setVersion(dubboProperties.getApplication().getVersion());
        applicationConfig.setOrganization(dubboProperties.getApplication().getOrganization());
        applicationConfig.setEnvironment(dubboProperties.getApplication().getEnvironment());
        applicationConfig.setOwner(dubboProperties.getApplication().getOwner());
        applicationConfig.setParameters(dubboProperties.getApplication().getParameters());
        return applicationConfig;
    }

    /**
     * 配置注册中心
     */
    @Bean
    @ConditionalOnMissingBean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(dubboProperties.getRegistry().getAddress());
        registryConfig.setUsername(dubboProperties.getRegistry().getUsername());
        registryConfig.setPassword(dubboProperties.getRegistry().getPassword());
        registryConfig.setTimeout((int) dubboProperties.getRegistry().getTimeout().toMillis());
        registryConfig.setCluster(dubboProperties.getRegistry().getCluster());
        registryConfig.setGroup(dubboProperties.getRegistry().getGroup());
        registryConfig.setNamespace(dubboProperties.getRegistry().getNamespace());
        registryConfig.setParameters(dubboProperties.getRegistry().getParameters());
        return registryConfig;
    }

    /**
     * 配置协议
     */
    @Bean
    @ConditionalOnMissingBean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(dubboProperties.getProtocol().getName());
        protocolConfig.setPort(dubboProperties.getProtocol().getPort());
        protocolConfig.setHost(dubboProperties.getProtocol().getHost());
        protocolConfig.setThreadpool(dubboProperties.getProtocol().getThreadpool());
        protocolConfig.setThreads(dubboProperties.getProtocol().getThreads());
        protocolConfig.setQueues(dubboProperties.getProtocol().getQueues());
        protocolConfig.setSerialization(dubboProperties.getProtocol().getSerialization());
        protocolConfig.setTimeout((int) dubboProperties.getProtocol().getTimeout().toMillis());
        protocolConfig.setConnections(dubboProperties.getProtocol().getConnections());
        protocolConfig.setWeight(dubboProperties.getProtocol().getWeight());
        protocolConfig.setParameters(dubboProperties.getProtocol().getParameters());
        return protocolConfig;
    }

    /**
     * 配置服务提供者
     */
    @Bean
    @ConditionalOnMissingBean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout((int) dubboProperties.getProvider().getTimeout().toMillis());
        providerConfig.setRetries(dubboProperties.getProvider().getRetries());
        providerConfig.setLoadbalance(dubboProperties.getProvider().getLoadbalance());
        providerConfig.setCluster(dubboProperties.getProvider().getCluster());
        providerConfig.setWeight(dubboProperties.getProvider().getWeight());
        providerConfig.setGroup(dubboProperties.getProvider().getGroup());
        providerConfig.setVersion(dubboProperties.getProvider().getVersion());
        providerConfig.setParameters(dubboProperties.getProvider().getParameters());
        return providerConfig;
    }

    /**
     * 配置服务消费者
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout((int) dubboProperties.getConsumer().getTimeout().toMillis());
        consumerConfig.setRetries(dubboProperties.getConsumer().getRetries());
        consumerConfig.setLoadbalance(dubboProperties.getConsumer().getLoadbalance());
        consumerConfig.setCluster(dubboProperties.getConsumer().getCluster());
        consumerConfig.setGroup(dubboProperties.getConsumer().getGroup());
        consumerConfig.setVersion(dubboProperties.getConsumer().getVersion());
        consumerConfig.setCheck(dubboProperties.getConsumer().getCheck());
        consumerConfig.setAsync(dubboProperties.getConsumer().getAsync());
        consumerConfig.setParameters(dubboProperties.getConsumer().getParameters());
        return consumerConfig;
    }
}
