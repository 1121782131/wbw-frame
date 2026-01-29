// 测试BusinessException
package com.wbw.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {
    
    @Test
    void testBusinessException() {
        BusinessException exception = new BusinessException(400, "参数错误");
        assertEquals(400, exception.getCode());
        assertEquals("参数错误", exception.getMessage());
    }
    
    @Test
    void testFactoryMethods() {
        BusinessException badRequest = BusinessException.badRequest("参数错误");
        assertEquals(400, badRequest.getCode());
        
        BusinessException unauthorized = BusinessException.unauthorized();
        assertEquals(401, unauthorized.getCode());
    }
}