// 测试Result
package com.wbw.common.result;

import com.wbw.common.result.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    
    @Test
    void testSuccess() {
        Result<String> result = Result.success("操作成功");
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNotNull(result.getTimestamp());
    }
    
    @Test
    void testError() {
        Result<String> result = Result.error(400, "参数错误");
        assertFalse(result.isSuccess());
        assertEquals(400, result.getCode());
        assertEquals("参数错误", result.getMessage());
    }
}
