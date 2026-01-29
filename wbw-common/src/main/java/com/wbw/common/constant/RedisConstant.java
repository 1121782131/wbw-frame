package com.wbw.common.constant;

/**
 * Redis相关常量
 */
public class RedisConstant {
    
    /**
     * Redis Key前缀相关
     */
    public static final String KEY_PREFIX = "wbw:";
    public static final String KEY_SEPARATOR = ":";
    
    /**
     * 锁相关
     */
    public static final String LOCK_KEY_PREFIX = "lock:";
    public static final long DEFAULT_LOCK_EXPIRE = 30L; // 默认锁过期时间30秒
    public static final long DEFAULT_LOCK_WAIT = 10L; // 默认等待锁时间10秒
    
    /**
     * 缓存时间相关（秒）
     */
    public static final long ONE_MINUTE = 60L;
    public static final long FIVE_MINUTES = 5 * 60L;
    public static final long TEN_MINUTES = 10 * 60L;
    public static final long HALF_HOUR = 30 * 60L;
    public static final long ONE_HOUR = 60 * 60L;
    public static final long TWO_HOURS = 2 * 60 * 60L;
    public static final long SIX_HOURS = 6 * 60 * 60L;
    public static final long ONE_DAY = 24 * 60 * 60L;
    public static final long ONE_WEEK = 7 * 24 * 60 * 60L;
    public static final long ONE_MONTH = 30 * 24 * 60 * 60L;
    
    /**
     * 缓存名称
     */
    public static final String CACHE_USER = "user";
    public static final String CACHE_PERMISSION = "permission";
    public static final String CACHE_CONFIG = "config";
    public static final String CACHE_DICT = "dict";
    public static final String CACHE_CAPTCHA = "captcha";
    
    /**
     * 分布式锁Key
     */
    public static final String LOCK_GENERATE_ID = "lock:generate:id:";
    public static final String LOCK_ORDER = "lock:order:";
    public static final String LOCK_SEC_KILL = "lock:sec:kill:";
    
    private RedisConstant() {
        // 防止实例化
    }
}