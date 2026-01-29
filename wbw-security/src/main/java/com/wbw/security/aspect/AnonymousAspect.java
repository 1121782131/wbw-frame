// com/wbw/jwt/aspect/AnonymousAspect.java
package com.wbw.security.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 匿名注解切面
 * 用于处理@Anonymous注解（可选，如果使用Gateway验证可以不用）
 */
@Slf4j
@Aspect
@Component
public class AnonymousAspect {
    
    /**
     * 匿名注解切点
     */
    @Pointcut("@annotation(com.wbw.security.annotation.Anonymous)")
    public void anonymousPointcut() {}
    
    /**
     * 类级别的匿名注解切点
     */
    @Pointcut("@within(com.wbw.security.annotation.Anonymous)")
    public void anonymousTypePointcut() {}
    
    /**
     * 处理匿名注解
     */
    @Around("anonymousPointcut() || anonymousTypePointcut()")
    public Object handleAnonymous(ProceedingJoinPoint joinPoint) throws Throwable {
        // 这里可以添加一些日志或其他处理
        // 由于是白名单接口，直接放行
        log.debug("执行白名单接口: {}", getMethodName(joinPoint));
        return joinPoint.proceed();
    }
    
    private String getMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringType().getSimpleName() + "." + signature.getName();
    }
}