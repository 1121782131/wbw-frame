package com.wbw.redis.util;

import com.wbw.common.constant.RedisConstant;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Redis Key构建工具类
 */
public class RedisKeyBuilder {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    /**
     * 构建完整的Key
     */
    public static String buildKey(String... parts) {
        return StringUtils.joinWith(RedisConstant.KEY_SEPARATOR, parts);
    }
    
    /**
     * 构建带前缀的Key
     */
    public static String buildKeyWithPrefix(String prefix, String... parts) {
        return prefix + buildKey(parts);
    }
    
    /**
     * 构建用户相关的Key
     */
    public static String buildUserKey(String userId) {
        return buildKey(RedisConstant.CACHE_USER, userId);
    }
    
    /**
     * 构建用户权限Key
     */
    public static String buildUserPermissionKey(String userId) {
        return buildKey(RedisConstant.CACHE_PERMISSION, userId);
    }
    
    /**
     * 构建配置Key
     */
    public static String buildConfigKey(String configKey) {
        return buildKey(RedisConstant.CACHE_CONFIG, configKey);
    }
    
    /**
     * 构建字典Key
     */
    public static String buildDictKey(String dictType) {
        return buildKey(RedisConstant.CACHE_DICT, dictType);
    }
    
    /**
     * 构建验证码Key
     */
    public static String buildCaptchaKey(String captchaId) {
        return buildKey(RedisConstant.CACHE_CAPTCHA, captchaId);
    }
    
    /**
     * 构建每日统计Key
     */
    public static String buildDailyStatKey(String statType) {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        return buildKey(statType, "daily", dateStr);
    }
    
    /**
     * 构建月统计Key
     */
    public static String buildMonthlyStatKey(String statType) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        return buildKey(statType, "monthly", dateStr);
    }
    
    /**
     * 构建限流Key
     */
    public static String buildRateLimitKey(String resource, String identifier) {
        return buildKey("rate:limit", resource, identifier);
    }
    
    /**
     * 构建会话Key
     */
    public static String buildSessionKey(String sessionId) {
        return buildKey("session", sessionId);
    }
    
    /**
     * 构建消息队列Key
     */
    public static String buildMessageQueueKey(String queueName) {
        return buildKey("mq", queueName);
    }
    
    /**
     * 构建分布式锁Key
     */
    public static String buildLockKey(String lockName) {
        return buildKey(RedisConstant.LOCK_KEY_PREFIX, lockName);
    }
    
    /**
     * 构建订单锁Key
     */
    public static String buildOrderLockKey(String orderId) {
        return buildKey(RedisConstant.LOCK_ORDER, orderId);
    }
    
    /**
     * 构建秒杀锁Key
     */
    public static String buildSecKillLockKey(String productId) {
        return buildKey(RedisConstant.LOCK_SEC_KILL, productId);
    }
}