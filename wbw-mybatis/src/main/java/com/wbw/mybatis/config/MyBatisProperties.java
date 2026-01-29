package com.wbw.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MyBatis 配置属性
 */
@Data
@ConfigurationProperties(prefix = "wbw.mybatis")
public class MyBatisProperties {
    
    /**
     * 是否开启SQL日志
     * 默认：true
     */
    private boolean showSql = true;
    
    /**
     * 是否开启驼峰命名映射
     * 默认：true
     */
    private boolean mapUnderscoreToCamelCase = true;
    
    /**
     * 分页最大限制
     * 默认：1000
     */
    private long maxLimit = 1000L;
    
    /**
     * 全局表前缀
     * 默认：空
     */
    private String globalTablePrefix = "";
    
    /**
     * 全局表后缀
     * 默认：空
     */
    private String globalTableSuffix = "";
    
    /**
     * Mapper扫描包路径
     * 默认：com.wbw.**.mapper
     */
    private String mapperScan = "com.wbw.**.mapper";
    
    /**
     * 是否启用动态数据源
     * 默认：false
     */
    private boolean dynamicDataSourceEnabled = false;
    
    /**
     * 动态数据源配置
     */
    private DynamicDataSourceProperties dynamicDataSource;
}