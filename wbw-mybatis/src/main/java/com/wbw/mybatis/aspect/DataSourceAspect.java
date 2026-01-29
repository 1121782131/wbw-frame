package com.wbw.mybatis.aspect;

import com.wbw.mybatis.annotation.DataSource;
import com.wbw.mybatis.datasource.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面
 * 处理@DataSource注解，实现数据源切换
 */
@Aspect
@Component
@Order(-1) // 优先级高于事务切面
public class DataSourceAspect {
    
    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.wbw.mybatis.annotation.DataSource) || @within(com.wbw.mybatis.annotation.DataSource)")
    public void dataSourcePointcut() {
    }
    
    /**
     * 环绕通知，处理数据源切换
     * 
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("dataSourcePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取原始数据源名称
        String originalDataSource = DataSourceContextHolder.getDataSourceName();
        
        try {
            // 解析数据源注解
            DataSource dataSourceAnnotation = getDataSourceAnnotation(joinPoint);
            if (dataSourceAnnotation != null) {
                // 设置目标数据源
                DataSourceContextHolder.setDataSourceName(dataSourceAnnotation.value());
            }
            
            // 执行方法
            return joinPoint.proceed();
        } finally {
            // 恢复原始数据源
            if (originalDataSource != null) {
                DataSourceContextHolder.setDataSourceName(originalDataSource);
            } else {
                DataSourceContextHolder.clearDataSourceName();
            }
        }
    }
    
    /**
     * 获取数据源注解
     * 
     * @param joinPoint 连接点
     * @return DataSource注解
     */
    private DataSource getDataSourceAnnotation(ProceedingJoinPoint joinPoint) {
        // 首先获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DataSource dataSourceAnnotation = method.getAnnotation(DataSource.class);
        
        // 如果方法上没有注解，获取类上的注解
        if (dataSourceAnnotation == null) {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            dataSourceAnnotation = targetClass.getAnnotation(DataSource.class);
        }
        
        return dataSourceAnnotation;
    }
}
