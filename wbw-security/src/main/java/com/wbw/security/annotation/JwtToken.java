// com/wbw/jwt/annotation/JwtToken.java
package com.wbw.security.annotation;

import java.lang.annotation.*;

/**
 * JWT Token注解
 * 用于标识需要生成或刷新Token的接口
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtToken {
    
    /**
     * Token类型
     */
    TokenType type() default TokenType.ACCESS;
    
    /**
     * 是否自动刷新（当Token即将过期时）
     */
    boolean autoRefresh() default false;
    
    /**
     * 刷新阈值（分钟，当Token在此时间内过期时自动刷新）
     */
    int refreshThreshold() default 30;
    
    enum TokenType {
        ACCESS,     // 访问令牌
        REFRESH     // 刷新令牌
    }
}