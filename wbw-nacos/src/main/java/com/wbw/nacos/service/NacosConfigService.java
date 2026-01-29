package com.wbw.nacos.service;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Nacos配置中心服务类
 * 提供配置获取、监听和管理功能
 */
@Slf4j
@Service
public class NacosConfigService {
    
    private final NacosConfigManager nacosConfigManager;
    private final NacosConfigProperties nacosConfigProperties;
    private final Executor executor = Executors.newSingleThreadExecutor();
    
    public NacosConfigService(NacosConfigManager nacosConfigManager, NacosConfigProperties nacosConfigProperties) {
        this.nacosConfigManager = nacosConfigManager;
        this.nacosConfigProperties = nacosConfigProperties;
    }
    
    /**
     * 获取ConfigService实例
     * @return ConfigService实例
     */
    private ConfigService getConfigService() {
        return nacosConfigManager.getConfigService();
    }
    
    /**
     * 获取配置
     * @param dataId 数据ID
     * @param group 分组
     * @param timeoutMs 超时时间（毫秒）
     * @return 配置内容
     * @throws NacosException Nacos异常
     */
    public String getConfig(String dataId, String group, long timeoutMs) throws NacosException {
        return getConfigService().getConfig(dataId, group, timeoutMs);
    }
    
    /**
     * 获取配置（使用默认分组和超时时间）
     * @param dataId 数据ID
     * @return 配置内容
     * @throws NacosException Nacos异常
     */
    public String getConfig(String dataId) throws NacosException {
        return getConfig(dataId, nacosConfigProperties.getGroup(), nacosConfigProperties.getTimeout());
    }
    
    /**
     * 发布配置
     * @param dataId 数据ID
     * @param group 分组
     * @param content 配置内容
     * @return 是否成功
     * @throws NacosException Nacos异常
     */
    public boolean publishConfig(String dataId, String group, String content) throws NacosException {
        boolean result = getConfigService().publishConfig(dataId, group, content);
        if (result) {
            log.info("Config published successfully: {}:{}", group, dataId);
        } else {
            log.warn("Config publish failed: {}:{}", group, dataId);
        }
        return result;
    }
    
    /**
     * 发布配置（使用默认分组）
     * @param dataId 数据ID
     * @param content 配置内容
     * @return 是否成功
     * @throws NacosException Nacos异常
     */
    public boolean publishConfig(String dataId, String content) throws NacosException {
        return publishConfig(dataId, nacosConfigProperties.getGroup(), content);
    }
    
    /**
     * 删除配置
     * @param dataId 数据ID
     * @param group 分组
     * @return 是否成功
     * @throws NacosException Nacos异常
     */
    public boolean removeConfig(String dataId, String group) throws NacosException {
        boolean result = getConfigService().removeConfig(dataId, group);
        if (result) {
            log.info("Config removed successfully: {}:{}", group, dataId);
        } else {
            log.warn("Config remove failed: {}:{}", group, dataId);
        }
        return result;
    }
    
    /**
     * 删除配置（使用默认分组）
     * @param dataId 数据ID
     * @return 是否成功
     * @throws NacosException Nacos异常
     */
    public boolean removeConfig(String dataId) throws NacosException {
        return removeConfig(dataId, nacosConfigProperties.getGroup());
    }
    
    /**
     * 监听配置变更
     * @param dataId 数据ID
     * @param group 分组
     * @param listener 变更监听器
     * @throws NacosException Nacos异常
     */
    public void addListener(String dataId, String group, Listener listener) throws NacosException {
        getConfigService().addListener(dataId, group, listener);
        log.info("Config listener added: {}:{}", group, dataId);
    }
    
    /**
     * 监听配置变更（使用默认分组）
     * @param dataId 数据ID
     * @param listener 变更监听器
     * @throws NacosException Nacos异常
     */
    public void addListener(String dataId, Listener listener) throws NacosException {
        addListener(dataId, nacosConfigProperties.getGroup(), listener);
    }
    
    /**
     * 移除配置监听器
     * @param dataId 数据ID
     * @param group 分组
     * @param listener 变更监听器
     */
    public void removeListener(String dataId, String group, Listener listener) {
        getConfigService().removeListener(dataId, group, listener);
        log.info("Config listener removed: {}:{}", group, dataId);
    }
    
    /**
     * 移除配置监听器（使用默认分组）
     * @param dataId 数据ID
     * @param listener 变更监听器
     */
    public void removeListener(String dataId, Listener listener) {
        removeListener(dataId, nacosConfigProperties.getGroup(), listener);
    }
    
    /**
     * 获取配置信息（简化版本，返回配置字符串）
     * @param dataId 数据ID
     * @param group 分组
     * @return 配置信息
     * @throws NacosException Nacos异常
     */
    public String getConfigInfo(String dataId, String group) throws NacosException {
        return getConfig(dataId, group, nacosConfigProperties.getTimeout());
    }
    
    /**
     * 获取配置信息（使用默认分组）
     * @param dataId 数据ID
     * @return 配置信息
     * @throws NacosException Nacos异常
     */
    public String getConfigInfo(String dataId) throws NacosException {
        return getConfigInfo(dataId, nacosConfigProperties.getGroup());
    }
    
    /**
     * 创建默认的配置监听器
     * @param configChangeCallback 配置变更回调
     * @return 监听器实例
     */
    public Listener createDefaultListener(ConfigChangeCallback configChangeCallback) {
        return new Listener() {
            @Override
            public Executor getExecutor() {
                return executor;
            }
            
            @Override
            public void receiveConfigInfo(String configInfo) {
                try {
                    configChangeCallback.onConfigChange(configInfo);
                } catch (Exception e) {
                    log.error("Error processing config change", e);
                }
            }
        };
    }
    
    /**
     * 配置变更回调接口
     */
    @FunctionalInterface
    public interface ConfigChangeCallback {
        void onConfigChange(String configInfo);
    }
}
