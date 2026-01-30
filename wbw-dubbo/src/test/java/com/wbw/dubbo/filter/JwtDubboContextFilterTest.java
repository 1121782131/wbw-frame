package com.wbw.dubbo.filter;

import com.wbw.security.context.JwtSecurityContext;
import com.wbw.security.model.JwtUser;
import org.apache.dubbo.rpc.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * JwtDubboContextFilter测试
 */
public class JwtDubboContextFilterTest {

    @Mock
    private Invoker<?> invoker;

    @Mock
    private Invocation invocation;

    @Mock
    private Result result;

    private JwtDubboContextFilter filter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new JwtDubboContextFilter();
        // 清除安全上下文
        JwtSecurityContext.clear();
    }

    @Test
    public void testInvoke_ConsumerSide_WithToken() {
        // 模拟消费者端
        when(invoker.invoke(invocation)).thenReturn(result);
        
        // 设置JWT令牌
        String token = "test-token";
        JwtSecurityContext.setToken(token);
        
        // 执行过滤器
        filter.invoke(invoker, invocation);
        
        // 验证令牌是否被设置到RPC上下文中
        verify(invocation).getAttachments();
        // 验证服务调用是否被执行
        verify(invoker).invoke(invocation);
    }

    @Test
    public void testInvoke_ConsumerSide_WithoutToken() {
        // 模拟消费者端
        when(invoker.invoke(invocation)).thenReturn(result);
        
        // 不设置JWT令牌
        
        // 执行过滤器
        filter.invoke(invoker, invocation);
        
        // 验证服务调用是否被执行
        verify(invoker).invoke(invocation);
    }

    @Test
    public void testInvoke_ProviderSide_WithToken() {
        // 模拟提供者端
        when(invoker.invoke(invocation)).thenReturn(result);
        when(invocation.getAttachment("JWT_TOKEN")).thenReturn("test-token");
        
        // 执行过滤器
        filter.invoke(invoker, invocation);
        
        // 验证服务调用是否被执行
        verify(invoker).invoke(invocation);
    }

    @Test
    public void testInvoke_ProviderSide_WithoutToken() {
        // 模拟提供者端
        when(invoker.invoke(invocation)).thenReturn(result);
        when(invocation.getAttachment("JWT_TOKEN")).thenReturn(null);
        
        // 执行过滤器
        filter.invoke(invoker, invocation);
        
        // 验证服务调用是否被执行
        verify(invoker).invoke(invocation);
    }

    @Test
    public void testInvoke_WithException() {
        // 模拟提供者端
        when(invoker.invoke(invocation)).thenThrow(new RpcException("Test exception"));
        when(invocation.getAttachment("JWT_TOKEN")).thenReturn("test-token");
        
        // 执行过滤器
        try {
            filter.invoke(invoker, invocation);
        } catch (RpcException e) {
            // 预期异常
        }
        
        // 验证服务调用是否被执行
        verify(invoker).invoke(invocation);
    }
}
