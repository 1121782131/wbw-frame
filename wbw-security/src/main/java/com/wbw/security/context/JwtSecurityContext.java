// com/wbw/jwt/context/JwtSecurityContext.java
package com.wbw.security.context;

import com.wbw.security.model.JwtUser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT安全上下文
 * 线程安全的用户信息存储
 */
@Slf4j
public class JwtSecurityContext {
    
    private static final ThreadLocal<JwtUser> USER_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> ATTRIBUTES_HOLDER = new ThreadLocal<>();
    
    private JwtSecurityContext() {
        // 防止实例化
    }
    
    /**
     * 设置当前用户
     */
    public static void setUser(JwtUser user) {
        USER_HOLDER.set(user);
        log.debug("设置用户到上下文: {}", user != null ? user.getUsername() : "null");
    }
    
    /**
     * 获取当前用户
     */
    public static JwtUser getUser() {
        return USER_HOLDER.get();
    }
    
    /**
     * 获取当前用户ID
     */
    public static String getUserId() {
        JwtUser user = getUser();
        return user != null ? user.getUserId() : null;
    }
    
    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        JwtUser user = getUser();
        return user != null ? user.getUsername() : null;
    }
    
    /**
     * 获取当前用户角色
     */
    public static String getRole() {
        JwtUser user = getUser();
        return user != null ? user.getRole() : null;
    }
    
    /**
     * 获取当前租户ID
     */
    public static String getTenantId() {
        JwtUser user = getUser();
        return user != null ? user.getTenantId() : null;
    }
    
    /**
     * 是否为管理员
     */
    public static boolean isAdmin() {
        JwtUser user = getUser();
        return user != null && user.isAdmin();
    }
    
    /**
     * 获取额外信息
     */
    public static Map<String, Object> getExtra() {
        JwtUser user = getUser();
        return user != null ? user.getExtra() : null;
    }
    
    /**
     * 获取特定额外信息
     */
    public static Object getExtra(String key) {
        Map<String, Object> extra = getExtra();
        return extra != null ? extra.get(key) : null;
    }
    
    /**
     * 设置当前Token
     */
    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
    }
    
    /**
     * 获取当前Token
     */
    public static String getToken() {
        return TOKEN_HOLDER.get();
    }
    
    /**
     * 设置属性
     */
    public static void setAttribute(String key, Object value) {
        Map<String, Object> attributes = ATTRIBUTES_HOLDER.get();
        if (attributes == null) {
            attributes = new HashMap<>();
            ATTRIBUTES_HOLDER.set(attributes);
        }
        attributes.put(key, value);
    }
    
    /**
     * 获取属性
     */
    public static Object getAttribute(String key) {
        Map<String, Object> attributes = ATTRIBUTES_HOLDER.get();
        return attributes != null ? attributes.get(key) : null;
    }
    
    /**
     * 清除上下文
     */
    public static void clear() {
        USER_HOLDER.remove();
        TOKEN_HOLDER.remove();
        ATTRIBUTES_HOLDER.remove();
        log.debug("安全上下文已清除");
    }
    
    /**
     * 检查是否已认证
     */
    public static boolean isAuthenticated() {
        return getUser() != null;
    }
}