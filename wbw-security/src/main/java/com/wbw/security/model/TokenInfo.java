// com/wbw/jwt/core/model/TokenInfo.java
package com.wbw.security.model;

import lombok.Data;

import java.util.Date;

/**
 * Token信息
 */
@Data
public class TokenInfo {
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";
    
    /**
     * 过期时间（秒）
     */
    private Long expiresIn;
    
    /**
     * 刷新令牌过期时间（秒）
     */
    private Long refreshExpiresIn;
    
    /**
     * 过期时间点
     */
    private Date expiresAt;
    
    /**
     * 刷新令牌过期时间点
     */
    private Date refreshExpiresAt;
    
    /**
     * 用户信息
     */
    private JwtUser user;
}