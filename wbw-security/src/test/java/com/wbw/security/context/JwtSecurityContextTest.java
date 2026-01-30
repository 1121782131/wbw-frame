package com.wbw.security.context;

import com.wbw.security.model.JwtUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT安全上下文测试
 */
public class JwtSecurityContextTest {
    
    @BeforeEach
    @AfterEach
    public void clearContext() {
        // 每次测试前后清理上下文
        JwtSecurityContext.clear();
    }
    
    @Test
    public void testSetAndGetUser() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 从上下文获取用户
        JwtUser retrievedUser = JwtSecurityContext.getUser();
        
        // 验证结果
        assertNotNull(retrievedUser);
        assertEquals(user.getUserId(), retrievedUser.getUserId());
        assertEquals(user.getUsername(), retrievedUser.getUsername());
    }
    
    @Test
    public void testGetUserId() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 获取用户ID
        String userId = JwtSecurityContext.getUserId();
        
        // 验证结果
        assertNotNull(userId);
        assertEquals(user.getUserId(), userId);
    }
    
    @Test
    public void testGetUsername() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 获取用户名
        String username = JwtSecurityContext.getUsername();
        
        // 验证结果
        assertNotNull(username);
        assertEquals(user.getUsername(), username);
    }
    
    @Test
    public void testGetRole() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 获取用户角色
        String role = JwtSecurityContext.getRole();
        
        // 验证结果
        assertNotNull(role);
        assertEquals(user.getRole(), role);
    }
    
    @Test
    public void testGetTenantId() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 获取租户ID
        String tenantId = JwtSecurityContext.getTenantId();
        
        // 验证结果
        assertNotNull(tenantId);
        assertEquals(user.getTenantId(), tenantId);
    }
    
    @Test
    public void testIsAdmin() {
        // 创建测试用户
        JwtUser user = createTestUser();
        user.setAdmin(true);
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 验证是否为管理员
        assertTrue(JwtSecurityContext.isAdmin());
    }
    
    @Test
    public void testSetAndGetToken() {
        // 测试令牌
        String token = "test-token";
        
        // 设置令牌到上下文
        JwtSecurityContext.setToken(token);
        
        // 从上下文获取令牌
        String retrievedToken = JwtSecurityContext.getToken();
        
        // 验证结果
        assertNotNull(retrievedToken);
        assertEquals(token, retrievedToken);
    }
    
    @Test
    public void testSetAndGetAttribute() {
        // 测试属性
        String key = "test-key";
        String value = "test-value";
        
        // 设置属性到上下文
        JwtSecurityContext.setAttribute(key, value);
        
        // 从上下文获取属性
        Object retrievedValue = JwtSecurityContext.getAttribute(key);
        
        // 验证结果
        assertNotNull(retrievedValue);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testGetAttributes() {
        // 测试属性
        String key1 = "test-key-1";
        String value1 = "test-value-1";
        String key2 = "test-key-2";
        String value2 = "test-value-2";
        
        // 设置属性到上下文
        JwtSecurityContext.setAttribute(key1, value1);
        JwtSecurityContext.setAttribute(key2, value2);
        
        // 从上下文获取所有属性
        Map<String, Object> attributes = JwtSecurityContext.getAttributes();
        
        // 验证结果
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals(value1, attributes.get(key1));
        assertEquals(value2, attributes.get(key2));
    }
    
    @Test
    public void testRemoveAttribute() {
        // 测试属性
        String key = "test-key";
        String value = "test-value";
        
        // 设置属性到上下文
        JwtSecurityContext.setAttribute(key, value);
        
        // 验证属性存在
        assertNotNull(JwtSecurityContext.getAttribute(key));
        
        // 从上下文移除属性
        JwtSecurityContext.removeAttribute(key);
        
        // 验证属性已移除
        assertNull(JwtSecurityContext.getAttribute(key));
    }
    
    @Test
    public void testIsAuthenticated() {
        // 初始状态下，用户未认证
        assertFalse(JwtSecurityContext.isAuthenticated());
        
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 验证用户已认证
        assertTrue(JwtSecurityContext.isAuthenticated());
    }
    
    @Test
    public void testHasRole() {
        // 创建测试用户
        JwtUser user = createTestUser();
        user.setRole("ADMIN");
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 验证用户有ADMIN角色
        assertTrue(JwtSecurityContext.hasRole("ADMIN"));
        // 验证用户没有USER角色
        assertFalse(JwtSecurityContext.hasRole("USER"));
    }
    
    @Test
    public void testHasPermission() {
        // 创建测试用户
        JwtUser user = createTestUser();
        user.setPermissions(new String[] {"READ", "WRITE"});
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 验证用户有READ权限
        assertTrue(JwtSecurityContext.hasPermission("READ"));
        // 验证用户有WRITE权限
        assertTrue(JwtSecurityContext.hasPermission("WRITE"));
        // 验证用户没有DELETE权限
        assertFalse(JwtSecurityContext.hasPermission("DELETE"));
    }
    
    @Test
    public void testClear() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 设置用户到上下文
        JwtSecurityContext.setUser(user);
        
        // 设置令牌到上下文
        JwtSecurityContext.setToken("test-token");
        
        // 设置属性到上下文
        JwtSecurityContext.setAttribute("test-key", "test-value");
        
        // 验证上下文已设置
        assertNotNull(JwtSecurityContext.getUser());
        assertNotNull(JwtSecurityContext.getToken());
        assertNotNull(JwtSecurityContext.getAttribute("test-key"));
        
        // 清理上下文
        JwtSecurityContext.clear();
        
        // 验证上下文已清理
        assertNull(JwtSecurityContext.getUser());
        assertNull(JwtSecurityContext.getToken());
        assertNull(JwtSecurityContext.getAttribute("test-key"));
    }
    
    /**
     * 创建测试用户
     */
    private JwtUser createTestUser() {
        JwtUser user = new JwtUser();
        user.setUserId("123");
        user.setUsername("testuser");
        user.setRole("USER");
        user.setTenantId("tenant1");
        user.setAdmin(false);
        return user;
    }
}
