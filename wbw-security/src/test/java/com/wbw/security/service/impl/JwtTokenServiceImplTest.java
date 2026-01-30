package com.wbw.security.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.redis.service.RedisService;
import com.wbw.security.config.JwtProperties;
import com.wbw.security.model.JwtUser;
import com.wbw.security.model.TokenInfo;
import com.wbw.security.model.TokenRequest;
import com.wbw.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT Token服务实现测试
 */
public class JwtTokenServiceImplTest {
    
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private JwtProperties jwtProperties;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private RedisService redisService;
    
    private JwtTokenServiceImpl jwtTokenService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 配置JwtProperties
        when(jwtProperties.getPrefix()).thenReturn("Bearer ");
        
        jwtTokenService = new JwtTokenServiceImpl(jwtUtil, jwtProperties, objectMapper, redisService);
    }
    
    @Test
    public void testGenerateTokenWithUser() {
        // 创建测试用户
        JwtUser user = createTestUser();
        
        // 创建测试令牌信息
        TokenInfo expectedTokenInfo = createTestTokenInfo(user);
        
        // 模拟JwtUtil的行为
        when(jwtUtil.generateTokenInfo(user)).thenReturn(expectedTokenInfo);
        
        // 调用generateToken方法
        TokenInfo actualTokenInfo = jwtTokenService.generateToken(user);
        
        // 验证结果
        assertNotNull(actualTokenInfo);
        assertEquals(expectedTokenInfo.getAccessToken(), actualTokenInfo.getAccessToken());
        assertEquals(expectedTokenInfo.getRefreshToken(), actualTokenInfo.getRefreshToken());
        
        // 验证JwtUtil的方法被调用
        verify(jwtUtil, times(1)).generateTokenInfo(user);
    }
    
    @Test
    public void testValidateToken() {
        // 测试令牌
        String token = "test-token";
        
        // 模拟JwtUtil的行为
        when(jwtUtil.validateToken(token)).thenReturn(true);
        
        // 调用validateToken方法
        boolean result = jwtTokenService.validateToken(token);
        
        // 验证结果
        assertTrue(result);
        
        // 验证JwtUtil的方法被调用
        verify(jwtUtil, times(1)).validateToken(token);
    }
    
    @Test
    public void testParseToken() {
        // 测试令牌
        String token = "test-token";
        
        // 创建测试用户
        JwtUser expectedUser = createTestUser();
        
        // 模拟JwtUtil的行为
        when(jwtUtil.extractUser(token)).thenReturn(expectedUser);
        
        // 调用parseToken方法
        JwtUser actualUser = jwtTokenService.parseToken(token);
        
        // 验证结果
        assertNotNull(actualUser);
        assertEquals(expectedUser.getUserId(), actualUser.getUserId());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        
        // 验证JwtUtil的方法被调用
        verify(jwtUtil, times(1)).extractUser(token);
    }
    
    @Test
    public void testExtractToken() {
        // 测试授权头
        String authorizationHeader = "Bearer test-token";
        
        // 调用extractToken方法
        String token = jwtTokenService.extractToken(authorizationHeader);
        
        // 验证结果
        assertNotNull(token);
        assertEquals("test-token", token);
    }
    
    @Test
    public void testAddTokenToBlacklist() {
        // 测试令牌
        String token = "test-token";
        
        // 模拟JwtUtil的行为，返回一个有效的过期时间
        when(jwtUtil.getExpirationDate(anyString())).thenReturn(new Date(System.currentTimeMillis() + 3600 * 1000));
        
        // 模拟RedisService的行为
        doNothing().when(redisService).set(anyString(), any(), anyLong(), any(TimeUnit.class));
        
        // 调用addTokenToBlacklist方法
        jwtTokenService.addTokenToBlacklist(token);
        
        // 验证RedisService的方法被调用
        verify(redisService, times(1)).set(anyString(), any(), anyLong(), any(TimeUnit.class));
    }
    
    @Test
    public void testIsTokenInBlacklist() {
        // 测试令牌
        String token = "test-token";
        
        // 模拟RedisService的行为
        when(redisService.hasKey(anyString())).thenReturn(true);
        
        // 调用isTokenInBlacklist方法
        boolean result = jwtTokenService.isTokenInBlacklist(token);
        
        // 验证结果
        assertTrue(result);
        
        // 验证RedisService的方法被调用
        verify(redisService, times(1)).hasKey(anyString());
    }
    
    @Test
    public void testRemoveTokenFromBlacklist() {
        // 测试令牌
        String token = "test-token";
        
        // 模拟RedisService的行为
        when(redisService.delete(anyString())).thenReturn(true);
        
        // 调用removeTokenFromBlacklist方法
        jwtTokenService.removeTokenFromBlacklist(token);
        
        // 验证RedisService的方法被调用
        verify(redisService, times(1)).delete(anyString());
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
    
    /**
     * 创建测试令牌信息
     */
    private TokenInfo createTestTokenInfo(JwtUser user) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken("test-access-token");
        tokenInfo.setRefreshToken("test-refresh-token");
        tokenInfo.setExpiresIn(3600L);
        tokenInfo.setRefreshExpiresIn(86400L);
        tokenInfo.setUser(user);
        return tokenInfo;
    }
}
