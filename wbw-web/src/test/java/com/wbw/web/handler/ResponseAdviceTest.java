package com.wbw.web.handler;

import com.wbw.common.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ResponseAdvice测试
 */
public class ResponseAdviceTest {

    @Mock
    private MethodParameter returnType;

    @Mock
    private HttpMessageConverter<?> converter;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    private ResponseAdvice responseAdvice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        responseAdvice = new ResponseAdvice();
    }

    @Test
    public void testSupports() {
        // 测试supports方法是否返回true
        boolean result = responseAdvice.supports(returnType, (Class<? extends HttpMessageConverter<?>>) converter.getClass());
        assertTrue(result);
    }

    @Test
    public void testBeforeBodyWrite_WithResult() {
        // 测试当返回结果已经是Result类型时的处理
        Result<String> originalResult = Result.success("test");
        Object result = responseAdvice.beforeBodyWrite(
                originalResult, returnType, MediaType.APPLICATION_JSON,
                (Class<? extends HttpMessageConverter<?>>) converter.getClass(), request, response
        );
        // 验证返回结果是否与原始结果相同
        assertSame(originalResult, result);
    }

    @Test
    public void testBeforeBodyWrite_WithNull() {
        // 测试当返回结果为null时的处理
        Object result = responseAdvice.beforeBodyWrite(
                null, returnType, MediaType.APPLICATION_JSON,
                (Class<? extends HttpMessageConverter<?>>) converter.getClass(), request, response
        );
        // 验证返回结果是否是成功的Result
        assertNotNull(result);
        assertTrue(result instanceof Result);
        Result<?> resultObj = (Result<?>) result;
        assertTrue(resultObj.getSuccess());
    }

    @Test
    public void testBeforeBodyWrite_WithString() {
        // 测试当返回结果为String类型时的处理
        String originalString = "test string";
        Object result = responseAdvice.beforeBodyWrite(
                originalString, returnType, MediaType.APPLICATION_JSON,
                (Class<? extends HttpMessageConverter<?>>) converter.getClass(), request, response
        );
        // 验证返回结果是否与原始字符串相同
        assertSame(originalString, result);
    }

    @Test
    public void testBeforeBodyWrite_WithOtherType() {
        // 测试当返回结果为其他类型时的处理
        Integer originalValue = 123;
        Object result = responseAdvice.beforeBodyWrite(
                originalValue, returnType, MediaType.APPLICATION_JSON,
                (Class<? extends HttpMessageConverter<?>>) converter.getClass(), request, response
        );
        // 验证返回结果是否是包装后的Result
        assertNotNull(result);
        assertTrue(result instanceof Result);
        Result<?> resultObj = (Result<?>) result;
        assertTrue(resultObj.getSuccess());
        assertEquals(originalValue, resultObj.getData());
    }
}
