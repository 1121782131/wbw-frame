package com.wbw.mybatis.datasource;

/**
 * 数据源上下文持有器
 * 用于管理线程级别的数据源标识
 */
public class DataSourceContextHolder {
    
    /**
     * 线程本地变量，存储当前数据源标识
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
    
    /**
     * 设置数据源标识
     * 
     * @param dataSourceName 数据源名称
     */
    public static void setDataSourceName(String dataSourceName) {
        CONTEXT_HOLDER.set(dataSourceName);
    }
    
    /**
     * 获取当前数据源标识
     * 
     * @return 数据源名称
     */
    public static String getDataSourceName() {
        return CONTEXT_HOLDER.get();
    }
    
    /**
     * 清除数据源标识
     */
    public static void clearDataSourceName() {
        CONTEXT_HOLDER.remove();
    }
    
    /**
     * 检查是否设置了数据源标识
     * 
     * @return 是否设置了数据源标识
     */
    public static boolean hasDataSourceName() {
        return CONTEXT_HOLDER.get() != null;
    }
}
