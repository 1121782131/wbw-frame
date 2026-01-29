// 测试NumberUtil
package com.wbw.common.utils.number;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest {
    
    @Test
    void testAdd() {
        BigDecimal result = NumberUtil.add(10.5, 20.3);
        assertEquals(new BigDecimal("30.80"), result);
    }
    
    @Test
    void testToChinese() {
        assertEquals("一百二十三", NumberUtil.toChinese(123));
        assertEquals("一千二百三十四", NumberUtil.toChinese(1234));
        assertEquals("一万二千三百四十五", NumberUtil.toChinese(12345));
    }
}