// com/wbw/jwt/core/service/impl/JwtTokenServiceImpl.java
package com.wbw.security.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.security.config.JwtProperties;
import com.wbw.security.model.JwtUser;
import com.wbw.security.model.TokenInfo;
import com.wbw.security.model.TokenRequest;
import com.wbw.security.service.JwtTokenService;
import com.wbw.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * JWT Token服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    
    @Override
    public TokenInfo generateToken(JwtUser user) {
        return jwtUtil.generateTokenInfo(user);
    }
    
    @Override
    public TokenInfo generateToken(TokenRequest request) {
        JwtUser user = convertToJwtUser(request);
        return generateToken(user);
    }
    
    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
    
    @Override
    public JwtUser parseToken(String token) {
        return jwtUtil.extractUser(token);
    }
    
    @Override
    public TokenInfo refreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("刷新令牌无效或已过期");
        }
        
        JwtUser user = parseToken(refreshToken);
        if (user == null) {
            throw new IllegalArgumentException("刷新令牌解析失败");
        }
        
        return generateToken(user);
    }
    
    @Override
    public TokenInfo refreshToken(String refreshToken, JwtUser updatedUser) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("刷新令牌无效或已过期");
        }
        
        return generateToken(updatedUser);
    }
    
    @Override
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtProperties.getPrefix())) {
            return null;
        }
        
        return authorizationHeader.substring(jwtProperties.getPrefix().length()).trim();
    }
    
    @Override
    public Long getTokenExpiration(String token) {
        Date expiration = jwtUtil.getExpirationDate(token);
        if (expiration == null) {
            return null;
        }
        
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }
    
    /**
     * 转换请求到用户对象
     */
    private JwtUser convertToJwtUser(TokenRequest request) {
        JwtUser user = new JwtUser();
        user.setUserId(request.getUserId());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setTenantId(request.getTenantId());
        user.setAdmin(request.isAdmin());
        
        // 解析额外信息
        if (request.getExtra() != null && !request.getExtra().isEmpty()) {
            try {
                Map<String, Object> extra = objectMapper.readValue(
                    request.getExtra(), 
                    new TypeReference<Map<String, Object>>() {}
                );
                user.setExtra(extra);
            } catch (Exception e) {
                log.warn("解析额外信息失败: {}", e.getMessage());
            }
        }
        
        return user;
    }
}