package com.wbw.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 白名单注解
 * 用于标识无需认证的接口
 * 支持类级别和方法级别的注解使用
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Whitelist {
    
    /**
     * 描述
     */
    String value() default "";
    
    /**
     * 是否包含所有子路径
     * 仅对类级别注解有效
     */
    boolean includeSubPaths() default true;
}
