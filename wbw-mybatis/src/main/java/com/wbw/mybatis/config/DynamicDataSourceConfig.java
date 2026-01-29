package com.wbw.mybatis.config;

import com.wbw.mybatis.datasource.DynamicDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置类
 * 管理多数据源的初始化和配置
 */
@Configuration
@ConditionalOnProperty(prefix = "wbw.mybatis", name = "dynamic-data-source-enabled", havingValue = "true")
@EnableConfigurationProperties(MyBatisProperties.class)
public class DynamicDataSourceConfig {
    
    @Autowired
    private MyBatisProperties myBatisProperties;
    
    /**
     * 创建动态数据源
     * 
     * @return 动态数据源
     */
    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        
        // 初始化数据源
        Map<Object, Object> dataSourceMap = new HashMap<>();
        DynamicDataSourceProperties dynamicDataSourceProperties = myBatisProperties.getDynamicDataSource();
        
        if (dynamicDataSourceProperties != null && dynamicDataSourceProperties.getDatasources() != null) {
            // 创建各个数据源
            for (Map.Entry<String, DataSourceProperties> entry : dynamicDataSourceProperties.getDatasources().entrySet()) {
                String name = entry.getKey();
                DataSourceProperties properties = entry.getValue();
                DataSource dataSource = createDataSource(properties);
                dataSourceMap.put(name, dataSource);
            }
            
            // 设置默认数据源
            String primary = dynamicDataSourceProperties.getPrimary();
            if (primary != null && dataSourceMap.containsKey(primary)) {
                dynamicDataSource.setDefaultTargetDataSource(dataSourceMap.get(primary));
            } else if (!dataSourceMap.isEmpty()) {
                // 如果没有指定默认数据源，使用第一个
                dynamicDataSource.setDefaultTargetDataSource(dataSourceMap.values().iterator().next());
            }
        }
        
        // 设置数据源映射
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        
        return dynamicDataSource;
    }
    
    /**
     * 创建数据源
     * 
     * @param properties 数据源配置
     * @return 数据源
     */
    private DataSource createDataSource(DataSourceProperties properties) {
        // 使用HikariCP连接池
        HikariConfig hikariConfig = new HikariConfig();
        
        // 基本配置
        hikariConfig.setJdbcUrl(properties.getUrl());
        hikariConfig.setUsername(properties.getUsername());
        hikariConfig.setPassword(properties.getPassword());
        
        if (properties.getDriverClassName() != null) {
            hikariConfig.setDriverClassName(properties.getDriverClassName());
        }
        
        // 连接池配置
        if (properties.getPool() != null) {
            DataSourceProperties.PoolProperties pool = properties.getPool();
            hikariConfig.setMinimumIdle(pool.getMinimumIdle());
            hikariConfig.setMaximumPoolSize(pool.getMaximumPoolSize());
            hikariConfig.setConnectionTimeout(pool.getConnectionTimeout());
            hikariConfig.setIdleTimeout(pool.getIdleTimeout());
            hikariConfig.setMaxLifetime(pool.getMaxLifetime());
            if (pool.getConnectionTestQuery() != null) {
                hikariConfig.setConnectionTestQuery(pool.getConnectionTestQuery());
            }
        }
        
        // 其他自定义属性
        if (properties.getHikari() != null) {
            for (Map.Entry<String, Object> entry : properties.getHikari().entrySet()) {
                hikariConfig.addDataSourceProperty(entry.getKey(), entry.getValue());
            }
        }
        
        return new HikariDataSource(hikariConfig);
    }
    
    /**
     * 为MyBatis Plus提供数据源
     * 
     * @return 数据源
     */
    @Bean
    public DataSource dataSource() {
        return dynamicDataSource();
    }
}
