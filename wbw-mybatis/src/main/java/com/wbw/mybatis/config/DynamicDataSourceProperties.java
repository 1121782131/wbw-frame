package com.wbw.mybatis.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 动态数据源配置属性
 */
@Data
public class DynamicDataSourceProperties {
    
    /**
     * 默认数据源名称
     */
    private String primary = "master";
    
    /**
     * 数据源配置映射
     */
    private Map<String, DataSourceProperties> datasources;
    
    /**
     * 主从数据源配置
     */
    private MasterSlaveProperties masterSlave;
    
    /**
     * 主从数据源配置
     */
    @Data
    public static class MasterSlaveProperties {
        
        /**
         * 主数据源名称
         */
        private String master;
        
        /**
         * 从数据源名称列表
         */
        private List<String> slaves;
        
        /**
         * 从数据源选择策略：round_robin（轮询）、random（随机）
         */
        private String strategy = "round_robin";
        
        /**
         * 是否开启从数据源负载均衡
         */
        private boolean loadBalance = true;
    }
}
