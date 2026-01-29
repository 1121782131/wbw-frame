// com/wbw/jwt/filter/JwtAuthFilter.java
package com.wbw.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.security.annotation.Anonymous;
import com.wbw.security.config.JwtProperties;
import com.wbw.security.context.JwtSecurityContext;
import com.wbw.security.model.JwtUser;
import com.wbw.security.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JWT认证过滤器
 * 可选，如果使用单体应用可以启用此过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final List<HandlerMapping> handlerMappings;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 检查是否为白名单接口
            if (isWhiteListRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            // 提取Token
            String authHeader = request.getHeader(jwtProperties.getHeader());
            String token = jwtTokenService.extractToken(authHeader);
            
            if (token == null) {
                sendUnauthorized(response, "缺少访问令牌");
                return;
            }
            
            // 验证Token
            if (!jwtTokenService.validateToken(token)) {
                sendUnauthorized(response, "访问令牌无效或已过期");
                return;
            }
            
            // 解析用户信息
            JwtUser user = jwtTokenService.parseToken(token);
            if (user == null) {
                sendUnauthorized(response, "令牌解析失败");
                return;
            }
            
            // 设置到安全上下文
            JwtSecurityContext.setUser(user);
            JwtSecurityContext.setToken(token);
            
            log.debug("用户认证成功: {}", user.getUsername());
            
            // 继续处理
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            log.error("JWT认证过滤器异常: {}", e.getMessage(), e);
            sendUnauthorized(response, "认证处理异常");
        } finally {
            // 清除安全上下文
            JwtSecurityContext.clear();
        }
    }
    
    /**
     * 检查是否为白名单请求
     */
    private boolean isWhiteListRequest(HttpServletRequest request) {
        // 检查配置的白名单路径
        String requestUri = request.getRequestURI();
        for (String pattern : jwtProperties.getWhiteList()) {
            if (matchesPattern(requestUri, pattern)) {
                return true;
            }
        }
        
        // 检查是否是静态资源
        if (requestUri.matches(".*\\.(html|css|js|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf)$")) {
            return true;
        }
        
        // 检查是否有@Anonymous注解
        return hasAnonymousAnnotation(request);
    }
    
    /**
     * 检查是否有@Anonymous注解
     */
    private boolean hasAnonymousAnnotation(HttpServletRequest request) {
        try {
            for (HandlerMapping handlerMapping : handlerMappings) {
                HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);
                if (handlerChain != null && handlerChain.getHandler() instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handlerChain.getHandler();
                    
                    // 检查方法注解
                    if (handlerMethod.getMethod().isAnnotationPresent(Anonymous.class)) {
                        return true;
                    }
                    
                    // 检查类注解
                    if (handlerMethod.getBeanType().isAnnotationPresent(Anonymous.class)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.debug("检查注解异常: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * 发送未授权响应
     */
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> errorResponse = Map.of(
            "code", 401,
            "message", message,
            "timestamp", System.currentTimeMillis()
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    
    /**
     * 匹配URL模式
     */
    private boolean matchesPattern(String path, String pattern) {
        if (pattern.equals(path)) {
            return true;
        }
        
        if (pattern.endsWith("/**")) {
            String base = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(base);
        }
        
        if (pattern.contains("*")) {
            String regex = pattern.replace("*", ".*");
            return path.matches(regex);
        }
        
        return false;
    }
}