// com/wbw/jwt/util/JwtUtil.java
package com.wbw.security.util;

import com.wbw.security.config.JwtProperties;
import com.wbw.security.model.JwtUser;
import com.wbw.security.model.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 提供Token的核心操作
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    private final JwtProperties jwtProperties;
    
    /**
     * 生成密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretBytes());
    }
    
    /**
     * 生成访问令牌
     */
    public String generateAccessToken(JwtUser user) {
        return generateToken(user, jwtProperties.getAccessTokenExpire());
    }
    
    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(JwtUser user) {
        return generateToken(user, jwtProperties.getRefreshTokenExpire());
    }
    
    /**
     * 生成Token
     */
    private String generateToken(JwtUser user, Long expireSeconds) {
        Map<String, Object> claims = buildClaims(user);
        
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireSeconds * 1000);
        
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(user.getUserId())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 构建Claims
     */
    private Map<String, Object> buildClaims(JwtUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        
        if (user.getRole() != null) {
            claims.put("role", user.getRole());
        }
        
        if (user.getPermissions() != null) {
            claims.put("permissions", user.getPermissions());
        }
        
        if (user.getTenantId() != null) {
            claims.put("tenantId", user.getTenantId());
        }
        
        if (user.getDeptId() != null) {
            claims.put("deptId", user.getDeptId());
        }
        
        if (user.getExtra() != null && !user.getExtra().isEmpty()) {
            claims.put("extra", user.getExtra());
        }
        
        claims.put("admin", user.isAdmin());
        
        return claims;
    }
    
    /**
     * 解析Token
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("解析JWT令牌失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 验证Token
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }
            
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (Exception e) {
            log.debug("验证JWT令牌失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 从Token中提取用户信息
     */
    public JwtUser extractUser(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        
        JwtUser user = new JwtUser();
        user.setUserId(claims.getSubject());
        user.setUsername(claims.get("username", String.class));
        user.setRole(claims.get("role", String.class));
        user.setTenantId(claims.get("tenantId", String.class));
        user.setDeptId(claims.get("deptId", String.class));
        user.setAdmin(Boolean.TRUE.equals(claims.get("admin", Boolean.class)));
        
        // 提取权限列表
        Object permissions = claims.get("permissions");
        if (permissions instanceof String[]) {
            user.setPermissions((String[]) permissions);
        } else if (permissions instanceof Iterable) {
            // 处理列表类型
            // 这里可以根据实际情况调整
        }
        
        // 提取额外信息
        @SuppressWarnings("unchecked")
        Map<String, Object> extra = claims.get("extra", Map.class);
        if (extra != null) {
            user.setExtra(extra);
        }
        
        return user;
    }
    
    /**
     * 获取Token过期时间
     */
    public Date getExpirationDate(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getExpiration() : null;
    }
    
    /**
     * 判断Token是否即将过期
     */
    public boolean isTokenAboutToExpire(String token, int minutes) {
        Date expiration = getExpirationDate(token);
        if (expiration == null) {
            return false;
        }
        
        Date now = new Date();
        Date warningTime = new Date(now.getTime() + minutes * 60 * 1000);
        return expiration.before(warningTime);
    }
    
    /**
     * 刷新Token（保留原用户信息）
     */
    public String refreshToken(String token, JwtUser updatedUser) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        
        // 使用更新后的用户信息或原用户信息
        JwtUser user = updatedUser != null ? updatedUser : extractUser(token);
        if (user == null) {
            return null;
        }
        
        return generateAccessToken(user);
    }
    
    /**
     * 生成完整的Token信息（包含访问令牌和刷新令牌）
     */
    public TokenInfo generateTokenInfo(JwtUser user) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken(generateAccessToken(user));
        tokenInfo.setRefreshToken(generateRefreshToken(user));
        tokenInfo.setExpiresIn(jwtProperties.getAccessTokenExpire());
        tokenInfo.setRefreshExpiresIn(jwtProperties.getRefreshTokenExpire());
        tokenInfo.setExpiresAt(getExpirationDate(tokenInfo.getAccessToken()));
        tokenInfo.setRefreshExpiresAt(getExpirationDate(tokenInfo.getRefreshToken()));
        tokenInfo.setUser(user);
        
        return tokenInfo;
    }
}