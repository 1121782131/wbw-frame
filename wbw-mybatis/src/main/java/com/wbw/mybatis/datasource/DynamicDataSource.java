package com.wbw.mybatis.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 * 基于AbstractRoutingDataSource实现数据源路由
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    
    /**
     * 存储目标数据源
     */
    private Map<Object, Object> targetDataSources = new HashMap<>();
    
    /**
     * 确定当前线程使用的数据源标识
     * 
     * @return 数据源标识
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceName();
    }
    
    /**
     * 设置默认数据源
     * 
     * @param defaultTargetDataSource 默认数据源
     */
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }
    
    /**
     * 设置目标数据源映射
     * 
     * @param targetDataSources 数据源映射
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        // 刷新数据源缓存
        afterPropertiesSet();
    }
    
    /**
     * 添加数据源
     * 
     * @param key 数据源标识
     * @param dataSource 数据源
     */
    public void addDataSource(Object key, Object dataSource) {
        targetDataSources.put(key, dataSource);
        setTargetDataSources(targetDataSources);
    }
    
    /**
     * 移除数据源
     * 
     * @param key 数据源标识
     */
    public void removeDataSource(Object key) {
        targetDataSources.remove(key);
        setTargetDataSources(targetDataSources);
    }
    
    /**
     * 获取所有数据源
     * 
     * @return 数据源映射
     */
    public Map<Object, Object> getTargetDataSources() {
        return targetDataSources;
    }
}
