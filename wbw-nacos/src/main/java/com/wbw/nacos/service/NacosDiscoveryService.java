package com.wbw.nacos.service;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Nacos服务发现服务类
 * 提供服务注册、发现和管理功能
 */
@Slf4j
@Service
public class NacosDiscoveryService {
    
    private final NacosServiceManager nacosServiceManager;
    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    
    public NacosDiscoveryService(NacosServiceManager nacosServiceManager, NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.nacosServiceManager = nacosServiceManager;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }
    
    /**
     * 获取NamingService实例
     * @return NamingService实例
     */
    private NamingService getNamingService() {
        return nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
    }
    
    /**
     * 注册服务实例
     * @param serviceName 服务名
     * @param ip IP地址
     * @param port 端口
     * @param clusterName 集群名
     * @param metadata 元数据
     * @throws NacosException Nacos异常
     */
    public void registerInstance(String serviceName, String ip, int port, String clusterName, Map<String, String> metadata) throws NacosException {
        Instance instance = new Instance();
        instance.setIp(ip);
        instance.setPort(port);
        instance.setWeight(nacosDiscoveryProperties.getWeight());
        instance.setClusterName(clusterName);
        instance.setMetadata(metadata);
        instance.setEnabled(true);
        
        getNamingService().registerInstance(serviceName, nacosDiscoveryProperties.getGroup(), instance);
        log.info("Service registered: {} at {}:{}", serviceName, ip, port);
    }
    
    /**
     * 注销服务实例
     * @param serviceName 服务名
     * @param ip IP地址
     * @param port 端口
     * @throws NacosException Nacos异常
     */
    public void deregisterInstance(String serviceName, String ip, int port) throws NacosException {
        getNamingService().deregisterInstance(serviceName, nacosDiscoveryProperties.getGroup(), ip, port);
        log.info("Service deregistered: {} at {}:{}", serviceName, ip, port);
    }
    
    /**
     * 获取服务实例列表
     * @param serviceName 服务名
     * @return 服务实例列表
     * @throws NacosException Nacos异常
     */
    public List<Instance> getInstances(String serviceName) throws NacosException {
        return getNamingService().getAllInstances(serviceName, nacosDiscoveryProperties.getGroup());
    }
    
    /**
     * 获取服务实例列表（按集群过滤）
     * @param serviceName 服务名
     * @param clusters 集群列表
     * @return 服务实例列表
     * @throws NacosException Nacos异常
     */
    public List<Instance> getInstances(String serviceName, List<String> clusters) throws NacosException {
        return getNamingService().getAllInstances(serviceName, nacosDiscoveryProperties.getGroup(), clusters);
    }
    
    /**
     * 获取健康的服务实例列表
     * @param serviceName 服务名
     * @return 健康的服务实例列表
     * @throws NacosException Nacos异常
     */
    public List<Instance> getHealthyInstances(String serviceName) throws NacosException {
        return getNamingService().selectInstances(serviceName, nacosDiscoveryProperties.getGroup(), true);
    }
    
    /**
     * 获取健康的服务实例列表（按集群过滤）
     * @param serviceName 服务名
     * @param clusters 集群列表
     * @return 健康的服务实例列表
     * @throws NacosException Nacos异常
     */
    public List<Instance> getHealthyInstances(String serviceName, List<String> clusters) throws NacosException {
        return getNamingService().selectInstances(serviceName, nacosDiscoveryProperties.getGroup(), clusters, true);
    }
    
    /**
     * 选择一个服务实例（用于负载均衡）
     * @param serviceName 服务名
     * @return 选中的服务实例
     * @throws NacosException Nacos异常
     */
    public Instance selectOneHealthyInstance(String serviceName) throws NacosException {
        return getNamingService().selectOneHealthyInstance(serviceName, nacosDiscoveryProperties.getGroup());
    }
    
    /**
     * 获取所有服务名
     * @return 服务名列表
     * @throws NacosException Nacos异常
     */
    public List<String> getServices() throws NacosException {
        Object result = getNamingService().getServicesOfServer(1, Integer.MAX_VALUE, nacosDiscoveryProperties.getGroup());
        // 由于不同版本的Nacos API返回类型可能不同，这里做兼容处理
        if (result != null) {
            // 假设返回对象有getData方法
            try {
                java.lang.reflect.Method getDataMethod = result.getClass().getMethod("getData");
                Object data = getDataMethod.invoke(result);
                if (data instanceof List) {
                    return (List<String>) data;
                }
            } catch (Exception e) {
                log.warn("Error getting services data: {}", e.getMessage());
            }
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * 订阅服务变更
     * @param serviceName 服务名
     * @param listener 变更监听器
     * @throws NacosException Nacos异常
     */
    public void subscribe(String serviceName, com.alibaba.nacos.api.naming.listener.EventListener listener) throws NacosException {
        getNamingService().subscribe(serviceName, nacosDiscoveryProperties.getGroup(), listener);
        log.info("Subscribed to service: {}", serviceName);
    }
    
    /**
     * 取消订阅服务变更
     * @param serviceName 服务名
     * @param listener 变更监听器
     * @throws NacosException Nacos异常
     */
    public void unsubscribe(String serviceName, com.alibaba.nacos.api.naming.listener.EventListener listener) throws NacosException {
        getNamingService().unsubscribe(serviceName, nacosDiscoveryProperties.getGroup(), listener);
        log.info("Unsubscribed from service: {}", serviceName);
    }
}
