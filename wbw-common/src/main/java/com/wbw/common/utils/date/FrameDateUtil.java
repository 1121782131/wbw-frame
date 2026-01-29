// com/wbw/common/utils/date/DateUtil.java
package com.wbw.common.utils.date;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.wbw.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
@Slf4j
public final class FrameDateUtil {
    
    private FrameDateUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * 默认时区
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");
    
    /**
     * 默认格式
     */
    public static final String DEFAULT_DATE_FORMAT = CommonConstant.DateFormat.DATETIME;
    
    /**
     * 获取当前日期时间
     */
    public static Date now() {
        return new Date();
    }
    
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(DEFAULT_ZONE_ID);
    }
    
    public static LocalDate nowLocalDate() {
        return LocalDate.now(DEFAULT_ZONE_ID);
    }
    
    /**
     * 日期转换
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID);
    }
    
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate();
    }
    
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }
    
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }
    
    /**
     * 日期格式化
     */
    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }
    
    public static String format(Date date, String pattern) {
        if (date == null || StringUtils.isBlank(pattern)) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }
    
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATE_FORMAT);
    }
    
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || StringUtils.isBlank(pattern)) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    public static String format(LocalDate date) {
        return format(date, CommonConstant.DateFormat.DATE);
    }
    
    public static String format(LocalDate date, String pattern) {
        if (date == null || StringUtils.isBlank(pattern)) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 日期解析
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_DATE_FORMAT);
    }
    
    public static Date parse(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(pattern)) {
            return null;
        }
        try {
            return DateUtil.parse(dateStr, pattern);
        } catch (Exception e) {
            log.error("日期解析失败: dateStr={}, pattern={}", dateStr, pattern, e);
            return null;
        }
    }
    
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        return parseLocalDateTime(dateStr, DEFAULT_DATE_FORMAT);
    }
    
    public static LocalDateTime parseLocalDateTime(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(pattern)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("LocalDateTime解析失败: dateStr={}, pattern={}", dateStr, pattern, e);
            return null;
        }
    }
    
    public static LocalDate parseLocalDate(String dateStr) {
        return parseLocalDate(dateStr, CommonConstant.DateFormat.DATE);
    }
    
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(pattern)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("LocalDate解析失败: dateStr={}, pattern={}", dateStr, pattern, e);
            return null;
        }
    }
    
    /**
     * 获取时间戳
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }
    
    public static long getTimestamp(Date date) {
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }
    
    public static long getTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0L;
        }
        return dateTime.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
    }
    
    /**
     * 时间戳转日期
     */
    public static Date fromTimestamp(long timestamp) {
        return new Date(timestamp);
    }
    
    public static LocalDateTime fromTimestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE_ID);
    }
    
    /**
     * 获取一天的开始时间
     */
    public static Date getDayStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getDayEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    public static LocalDateTime getDayStart(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atStartOfDay();
    }
    
    public static LocalDateTime getDayEnd(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atTime(23, 59, 59, 999_999_999);
    }
    
    /**
     * 获取月份的开始和结束
     */
    public static Date getMonthStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getMonthEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    /**
     * 日期加减
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
    
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
    
    public static Date addYears(Date date, int years) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }
    
    public static LocalDateTime addDays(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusDays(days);
    }
    
    public static LocalDateTime addMonths(LocalDateTime dateTime, int months) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMonths(months);
    }
    
    /**
     * 计算日期差
     */
    public static long betweenDays(Date start, Date end) {
        if (start == null || end == null) {
            return 0L;
        }
        return Math.abs(DateUtil.between(start, end, DateUnit.DAY));
    }
    
    public static long betweenDays(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0L;
        }
        return Math.abs(ChronoUnit.DAYS.between(start, end));
    }
    
    public static long betweenHours(Date start, Date end) {
        if (start == null || end == null) {
            return 0L;
        }
        return Math.abs(DateUtil.between(start, end, DateUnit.HOUR));
    }
    
    /**
     * 判断日期是否在范围内
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.before(start) && !date.after(end);
    }
    
    public static boolean isBetween(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null || start == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }
    
    /**
     * 获取年龄
     */
    public static int getAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return DateUtil.ageOfNow(birthDate);
    }
    
    public static int getAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return DateUtil.isLeapYear(year);
    }
    
    /**
     * 获取本周的开始和结束
     */
    public static Date getWeekStart(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.beginOfWeek(date);
    }
    
    public static Date getWeekEnd(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.endOfWeek(date);
    }
    
    /**
     * 获取季度开始和结束
     */
    public static Date getQuarterStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int quarter = month / 3;
        calendar.set(Calendar.MONTH, quarter * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 获取年份开始和结束
     */
    public static Date getYearStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getYearEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    /**
     * 格式化时间间隔
     */
    public static String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        }
        
        long seconds = millis / 1000;
        if (seconds < 60) {
            return seconds + "s";
        }
        
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "m " + (seconds % 60) + "s";
        }
        
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "h " + (minutes % 60) + "m";
        }
        
        long days = hours / 24;
        return days + "d " + (hours % 24) + "h";
    }
}