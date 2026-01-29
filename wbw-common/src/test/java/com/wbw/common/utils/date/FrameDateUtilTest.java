// 测试FrameDateUtil
package com.wbw.common.utils.date;


import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FrameDateUtilTest {
    
    @Test
    void testFormat() {
        Date date = FrameDateUtil.parse("2023-12-01 10:30:00", "yyyy-MM-dd HH:mm:ss");
        assertNotNull(date);
        
        String formatted = FrameDateUtil.format(date, "yyyy年MM月dd日");
        System.out.println(formatted);
        assertEquals("2023年12月01日", formatted);

    }
    
    @Test
    void testAddDays() {
        Date date = FrameDateUtil.parse("2023-12-01", "yyyy-MM-dd");
        Date nextDay = FrameDateUtil.addDays(date, 1);
        String formatted = FrameDateUtil.format(nextDay, "yyyy-MM-dd");
        System.out.println(formatted);
        assertEquals("2023-12-02", formatted);
    }
}