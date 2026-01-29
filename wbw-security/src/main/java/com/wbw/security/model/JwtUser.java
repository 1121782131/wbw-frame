// com/wbw/jwt/core/model/JwtUser.java
package com.wbw.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT用户信息
 */
@Data
public class JwtUser implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
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
     * 权限列表
     */
    private String[] permissions;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 额外信息
     */
    private Map<String, Object> extra = new HashMap<>();
    
    /**
     * 是否为管理员
     */
    private boolean admin = false;
    
    /**
     * 获取用户标识
     */
    public String getIdentity() {
        return userId + "@" + tenantId;
    }
    
    /**
     * 添加额外信息
     */
    public JwtUser addExtra(String key, Object value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
        return this;
    }
    
    /**
     * 获取额外信息
     */
    public Object getExtra(String key) {
        return extra != null ? extra.get(key) : null;
    }
}