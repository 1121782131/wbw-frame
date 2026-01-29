package com.wbw.security.util;

import com.wbw.security.config.JwtProperties;
import com.wbw.security.model.JwtUser;
import com.wbw.security.model.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Token辅助工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenHelper {
    
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    
    /**
     * 从请求中提取Token
     */
    public String extractToken(HttpServletRequest request) {
        // 1. 从Header获取
        String authHeader = request.getHeader(jwtProperties.getHeader());
        if (authHeader != null && authHeader.startsWith(jwtProperties.getPrefix())) {
            return authHeader.substring(jwtProperties.getPrefix().length()).trim();
        }
        
        // 2. 从参数获取
        String tokenParam = request.getParameter("token");
        if (tokenParam != null && !tokenParam.isEmpty()) {
            return tokenParam;
        }
        
        // 3. 从Cookie获取
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 验证请求中的Token
     */
    public boolean validateRequest(HttpServletRequest request) {
        String token = extractToken(request);
        return token != null && jwtUtil.validateToken(token);
    }
    
    /**
     * 从请求中获取用户信息
     */
    public JwtUser getUserFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return null;
        }
        return jwtUtil.extractUser(token);
    }
    
    /**
     * 构建Token响应
     */
    public Map<String, Object> buildTokenResponse(TokenInfo tokenInfo) {
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", tokenInfo.getAccessToken());
        response.put("refreshToken", tokenInfo.getRefreshToken());
        response.put("tokenType", tokenInfo.getTokenType());
        response.put("expiresIn", tokenInfo.getExpiresIn());
        response.put("refreshExpiresIn", tokenInfo.getRefreshExpiresIn());
        
        if (tokenInfo.getUser() != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", tokenInfo.getUser().getUserId());
            userInfo.put("username", tokenInfo.getUser().getUsername());
            userInfo.put("role", tokenInfo.getUser().getRole());
            userInfo.put("tenantId", tokenInfo.getUser().getTenantId());
            userInfo.put("admin", tokenInfo.getUser().isAdmin());
            
            response.put("userInfo", userInfo);
        }
        
        return response;
    }
    
    /**
     * 构建错误响应
     */
    public Map<String, Object> buildErrorResponse(String message, int code) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}