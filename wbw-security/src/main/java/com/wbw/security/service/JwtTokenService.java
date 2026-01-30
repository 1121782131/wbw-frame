// com/wbw/jwt/core/service/JwtTokenService.java
package com.wbw.security.service;


import com.wbw.security.model.JwtUser;
import com.wbw.security.model.TokenInfo;
import com.wbw.security.model.TokenRequest;

/**
 * JWT Token服务接口
 */
public interface JwtTokenService {
    
    /**
     * 生成Token
     */
    TokenInfo generateToken(JwtUser user);
    
    /**
     * 根据请求生成Token
     */
    TokenInfo generateToken(TokenRequest request);
    
    /**
     * 验证Token
     */
    boolean validateToken(String token);
    
    /**
     * 解析Token获取用户信息
     */
    JwtUser parseToken(String token);
    
    /**
     * 刷新Token
     */
    TokenInfo refreshToken(String refreshToken);
    
    /**
     * 刷新Token（携带更新后的用户信息）
     */
    TokenInfo refreshToken(String refreshToken, JwtUser updatedUser);
    
    /**
     * 从请求头中提取Token
     */
    String extractToken(String authorizationHeader);
    
    /**
     * 获取Token过期时间
     */
    Long getTokenExpiration(String token);
    
    /**
     * 将Token加入黑名单
     */
    void addTokenToBlacklist(String token);
    
    /**
     * 检查Token是否在黑名单中
     */
    boolean isTokenInBlacklist(String token);
    
    /**
     * 从黑名单中移除Token
     */
    void removeTokenFromBlacklist(String token);
}