package com.wbw.security.annotation;

import java.lang.annotation.*;

/**
 * 白名单注解
 * 添加此注解的接口不需要Token校验
 * 可以在Gateway或Filter中使用此注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous {
    
    /**
     * 是否完全公开（默认true）
     */
    boolean value() default true;
}