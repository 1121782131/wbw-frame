package com.wbw.mybatis.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 事务切面
 * 提供事务执行的监控和日志
 * 使用Spring AOP实现
 */
@Aspect
@Component
@Order(1) // 设置切面执行顺序
@Slf4j
public class TransactionAspect {
    
    /**
     * 监控所有带有 @Transactional 注解的方法
     */
    @Around("@annotation(org.springframework.transaction.annotation.Transactional) || " +
            "@within(org.springframework.transaction.annotation.Transactional)")
    public Object monitorTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取方法名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        String fullMethodName = className + "." + methodName;
        
        // 获取 @Transactional 注解
        Transactional transactional = AnnotationUtils.findAnnotation(method, Transactional.class);
        if (transactional == null) {
            transactional = AnnotationUtils.findAnnotation(
                joinPoint.getTarget().getClass(), Transactional.class);
        }
        
        long startTime = System.currentTimeMillis();
        
        if (log.isDebugEnabled()) {
            log.debug("开始事务执行: {}, 传播行为: {}, 只读: {}", 
                     fullMethodName, 
                     transactional != null ? transactional.propagation().name() : "DEFAULT",
                     transactional != null ? transactional.readOnly() : false);
        }
        
        try {
            Object result = joinPoint.proceed();
            
            long endTime = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug("事务执行成功: {}, 耗时: {}ms", 
                         fullMethodName, endTime - startTime);
            }
            
            return result;
            
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            
            // 判断是否需要回滚
            boolean shouldRollback = shouldRollback(e, transactional);
            
            if (shouldRollback) {
                log.warn("事务执行失败，已回滚: {}, 耗时: {}ms, 异常: {}", 
                        fullMethodName, endTime - startTime, e.getClass().getSimpleName() + ": " + e.getMessage());
            } else {
                log.warn("事务执行失败，未回滚: {}, 耗时: {}ms, 异常: {}", 
                        fullMethodName, endTime - startTime, e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            
            throw e;
        }
    }
    
    /**
     * 判断异常是否需要回滚
     */
    private boolean shouldRollback(Throwable e, Transactional transactional) {
        if (transactional == null) {
            return true; // 默认回滚
        }
        
        // 检查是否需要回滚的异常类型
        for (Class<?> exClass : transactional.rollbackFor()) {
            if (exClass.isAssignableFrom(e.getClass())) {
                return true;
            }
        }
        
        // 检查不需要回滚的异常类型
        for (Class<?> exClass : transactional.noRollbackFor()) {
            if (exClass.isAssignableFrom(e.getClass())) {
                return false;
            }
        }
        
        // 默认回滚规则：RuntimeException和Error回滚，其他不回滚
        return e instanceof RuntimeException || e instanceof Error;
    }
}