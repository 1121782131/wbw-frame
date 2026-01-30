// com/wbw/jwt/core/service/impl/JwtTokenServiceImpl.java
package com.wbw.security.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.redis.service.RedisService;
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
import java.util.concurrent.TimeUnit;

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
    private final RedisService redisService;
    
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
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
        // 检查token是否在黑名单中
        if (isTokenInBlacklist(token)) {
            log.debug("令牌在黑名单中: {}", token.substring(0, 20) + "...");
            return false;
        }
        
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
        
        // 将旧令牌加入黑名单
        addTokenToBlacklist(refreshToken);
        
        return generateToken(user);
    }
    
    @Override
    public TokenInfo refreshToken(String refreshToken, JwtUser updatedUser) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("刷新令牌无效或已过期");
        }
        
        // 将旧令牌加入黑名单
        addTokenToBlacklist(refreshToken);
        
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
    
    @Override
    public void addTokenToBlacklist(String token) {
        try {
            // 计算token的过期时间
            Long expiration = getTokenExpiration(token);
            if (expiration == null || expiration <= 0) {
                String tokenPreview = token.length() > 20 ? token.substring(0, 20) + "..." : token;
                log.debug("令牌已过期，无需加入黑名单: {}", tokenPreview);
                return;
            }
            
            // 使用token的签名部分作为key，避免存储完整token
            String tokenKey = TOKEN_BLACKLIST_PREFIX + getTokenSignature(token);
            
            // 将token加入黑名单，并设置过期时间
            redisService.set(tokenKey, "1", expiration, TimeUnit.SECONDS);
            String tokenPreview = token.length() > 20 ? token.substring(0, 20) + "..." : token;
            log.debug("令牌已加入黑名单: {}", tokenPreview);
        } catch (Exception e) {
            log.error("将令牌加入黑名单失败: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isTokenInBlacklist(String token) {
        try {
            String tokenKey = TOKEN_BLACKLIST_PREFIX + getTokenSignature(token);
            return redisService.hasKey(tokenKey);
        } catch (Exception e) {
            log.error("检查令牌是否在黑名单中失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public void removeTokenFromBlacklist(String token) {
        try {
            String tokenKey = TOKEN_BLACKLIST_PREFIX + getTokenSignature(token);
            redisService.delete(tokenKey);
            String tokenPreview = token.length() > 20 ? token.substring(0, 20) + "..." : token;
            log.debug("令牌已从黑名单中移除: {}", tokenPreview);
        } catch (Exception e) {
            log.error("从黑名单中移除令牌失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 获取Token的签名部分
     */
    private String getTokenSignature(String token) {
        // JWT格式: header.payload.signature
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            return parts[2];
        }
        return token;
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