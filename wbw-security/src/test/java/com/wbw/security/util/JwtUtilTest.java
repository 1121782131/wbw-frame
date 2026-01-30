package com.wbw.security.util;

import com.wbw.security.config.JwtProperties;
import com.wbw.security.model.JwtUser;
import com.wbw.security.model.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * JWT工具类测试
 */
public class JwtUtilTest {
    
    @Mock
    private JwtProperties jwtProperties;
    
    private JwtUtil jwtUtil;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 配置JwtProperties
        when(jwtProperties.getSecretBytes()).thenReturn("test-secret-key-12345678901234567890123456789012".getBytes());
        when(jwtProperties.getAccessTokenExpire()).thenReturn(3600L);
        when(jwtProperties.getRefreshTokenExpire()).thenReturn(86400L);
        
        jwtUtil = new JwtUtil(jwtProperties);
    }
    
    @Test
    public void testGenerateAccessToken() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成访问令牌
        String token = jwtUtil.generateAccessToken(user);
        
        // 验证令牌不为空
        assertNotNull(token);
        
        // 验证令牌可以被解析
        JwtUser extractedUser = jwtUtil.extractUser(token);
        assertNotNull(extractedUser);
        assertEquals(user.getUserId(), extractedUser.getUserId());
        assertEquals(user.getUsername(), extractedUser.getUsername());
    }
    
    @Test
    public void testGenerateRefreshToken() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成刷新令牌
        String token = jwtUtil.generateRefreshToken(user);
        
        // 验证令牌不为空
        assertNotNull(token);
        
        // 验证令牌可以被解析
        JwtUser extractedUser = jwtUtil.extractUser(token);
        assertNotNull(extractedUser);
        assertEquals(user.getUserId(), extractedUser.getUserId());
        assertEquals(user.getUsername(), extractedUser.getUsername());
    }
    
    @Test
    public void testValidateToken() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成访问令牌
        String token = jwtUtil.generateAccessToken(user);
        
        // 验证令牌有效
        assertTrue(jwtUtil.validateToken(token));
    }
    
    @Test
    public void testExtractUser() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成访问令牌
        String token = jwtUtil.generateAccessToken(user);
        
        // 提取用户信息
        JwtUser extractedUser = jwtUtil.extractUser(token);
        
        // 验证提取的用户信息正确
        assertNotNull(extractedUser);
        assertEquals(user.getUserId(), extractedUser.getUserId());
        assertEquals(user.getUsername(), extractedUser.getUsername());
        assertEquals(user.getRole(), extractedUser.getRole());
        assertEquals(user.getTenantId(), extractedUser.getTenantId());
        assertEquals(user.isAdmin(), extractedUser.isAdmin());
    }
    
    @Test
    public void testGetExpirationDate() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成访问令牌
        String token = jwtUtil.generateAccessToken(user);
        
        // 获取过期时间
        Date expirationDate = jwtUtil.getExpirationDate(token);
        
        // 验证过期时间不为空
        assertNotNull(expirationDate);
        
        // 验证过期时间在未来
        assertTrue(expirationDate.after(new Date()));
    }
    
    @Test
    public void testIsTokenAboutToExpire() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成访问令牌
        String token = jwtUtil.generateAccessToken(user);
        
        // 验证令牌不会立即过期（使用10分钟作为阈值，小于默认的2小时过期时间）
        assertFalse(jwtUtil.isTokenAboutToExpire(token, 10));
    }
    
    @Test
    public void testRefreshToken() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成访问令牌
        String token = jwtUtil.generateAccessToken(user);
        
        // 创建更新后的用户信息（使用不同的用户名）
        JwtUser updatedUser = createTestUser();
        updatedUser.setUsername("updated-testuser");
        
        // 刷新令牌
        String refreshedToken = jwtUtil.refreshToken(token, updatedUser);
        
        // 验证刷新后的令牌不为空
        assertNotNull(refreshedToken);
        
        // 验证刷新后的令牌与原令牌不同
        assertNotEquals(token, refreshedToken);
        
        // 验证刷新后的令牌有效
        assertTrue(jwtUtil.validateToken(refreshedToken));
    }
    
    @Test
    public void testGenerateTokenInfo() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 生成令牌信息
        TokenInfo tokenInfo = jwtUtil.generateTokenInfo(user);
        
        // 验证令牌信息不为空
        assertNotNull(tokenInfo);
        assertNotNull(tokenInfo.getAccessToken());
        assertNotNull(tokenInfo.getRefreshToken());
        assertNotNull(tokenInfo.getExpiresIn());
        assertNotNull(tokenInfo.getRefreshExpiresIn());
        assertNotNull(tokenInfo.getExpiresAt());
        assertNotNull(tokenInfo.getRefreshExpiresAt());
        assertNotNull(tokenInfo.getUser());
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
        
        // 添加额外信息
        Map<String, Object> extra = new HashMap<>();
        extra.put("email", "test@example.com");
        extra.put("phone", "13800138000");
        user.setExtra(extra);
        
        return user;
    }
}
