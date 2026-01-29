// com/wbw/jwt/core/model/TokenRequest.java
package com.wbw.security.model;

import lombok.Data;

/**
 * Token请求参数
 */
@Data
public class TokenRequest {
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户角色
     */
    private String role;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 是否为管理员
     */
    private boolean admin = false;
    
    /**
     * 记住我（长过期时间）
     */
    private boolean rememberMe = false;
    
    /**
     * 额外信息（JSON格式）
     */
    private String extra;
}