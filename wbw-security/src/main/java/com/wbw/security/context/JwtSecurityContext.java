// com/wbw/jwt/context/JwtSecurityContext.java
package com.wbw.security.context;

import com.wbw.security.model.JwtUser;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT安全上下文
 * 线程安全的用户信息存储
 */
@Slf4j
public class JwtSecurityContext {
    
    private static final ThreadLocal<JwtUser> USER_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> ATTRIBUTES_HOLDER = ThreadLocal.withInitial(ConcurrentHashMap::new);
    
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
        return user != null ? user.getExtra() : Collections.emptyMap();
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
        ATTRIBUTES_HOLDER.get().put(key, value);
    }
    
    /**
     * 获取属性
     */
    public static Object getAttribute(String key) {
        return ATTRIBUTES_HOLDER.get().get(key);
    }
    
    /**
     * 获取所有属性
     */
    public static Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(ATTRIBUTES_HOLDER.get());
    }
    
    /**
     * 移除属性
     */
    public static void removeAttribute(String key) {
        ATTRIBUTES_HOLDER.get().remove(key);
    }
    
    /**
     * 清除上下文
     */
    public static void clear() {
        try {
            USER_HOLDER.remove();
            TOKEN_HOLDER.remove();
            ATTRIBUTES_HOLDER.remove();
            log.debug("安全上下文已清除");
        } catch (Exception e) {
            log.error("清除安全上下文失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 检查是否已认证
     */
    public static boolean isAuthenticated() {
        return getUser() != null;
    }
    
    /**
     * 检查是否有特定角色
     */
    public static boolean hasRole(String role) {
        JwtUser user = getUser();
        return user != null && role.equals(user.getRole());
    }
    
    /**
     * 检查是否有特定权限
     */
    public static boolean hasPermission(String permission) {
        JwtUser user = getUser();
        if (user == null) {
            return false;
        }
        
        String[] permissions = user.getPermissions();
        if (permissions == null) {
            return false;
        }
        
        for (String perm : permissions) {
            if (perm.equals(permission)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 获取当前上下文的快照
     * 用于跨线程传递
     */
    public static ContextSnapshot capture() {
        return new ContextSnapshot(
            USER_HOLDER.get(),
            TOKEN_HOLDER.get(),
            new HashMap<>(ATTRIBUTES_HOLDER.get())
        );
    }
    
    /**
     * 从快照中恢复上下文
     * 用于跨线程传递
     */
    public static void restore(ContextSnapshot snapshot) {
        if (snapshot != null) {
            setUser(snapshot.getUser());
            setToken(snapshot.getToken());
            ATTRIBUTES_HOLDER.get().putAll(snapshot.getAttributes());
            log.debug("从快照中恢复安全上下文: {}", snapshot.getUser() != null ? snapshot.getUser().getUsername() : "null");
        }
    }
    
    /**
     * 上下文快照
     * 用于跨线程传递
     */
    public static class ContextSnapshot implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private final JwtUser user;
        private final String token;
        private final Map<String, Object> attributes;
        
        ContextSnapshot(JwtUser user, String token, Map<String, Object> attributes) {
            this.user = user;
            this.token = token;
            this.attributes = attributes;
        }
        
        public JwtUser getUser() {
            return user;
        }
        
        public String getToken() {
            return token;
        }
        
        public Map<String, Object> getAttributes() {
            return Collections.unmodifiableMap(attributes);
        }
    }
}