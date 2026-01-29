// 测试StringUtil
package com.wbw.common.utils.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {
    
    @Test
    void testIsEmpty() {
        assertTrue(StringUtil.isEmpty(null));
        assertTrue(StringUtil.isEmpty(""));
        assertFalse(StringUtil.isEmpty("test"));
    }
    
    @Test
    void testHidePhone() {
        String phone = "13800138000";
        String hidden = StringUtil.hidePhone(phone);
        assertEquals("138****8000", hidden);
    }
    
    @Test
    void testCamelToUnderline() {
        String camel = "userName";
        String underline = StringUtil.camelToUnderline(camel);
        assertEquals("user_name", underline);
    }
}
