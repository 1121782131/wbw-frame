package com.wbw.redis.tool;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wbw.common.constant.RedisConstant;
import com.wbw.redis.service.RedisService;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分布式锁工具类
 */
@Component
public class RedisLock {
    
    private final RedisService redisService;
    private final Lock localLock = new ReentrantLock();
    
    public RedisLock(RedisService redisService) {
        this.redisService = redisService;
    }
    
    /**
     * 获取分布式锁
     */
    public String lock(String key) {
        return lock(key, RedisConstant.DEFAULT_LOCK_EXPIRE, TimeUnit.SECONDS);
    }
    
    /**
     * 获取分布式锁（带过期时间）
     */
    public String lock(String key, long expire, TimeUnit timeUnit) {
        String lockKey = buildLockKey(key);
        String lockValue = IdUtil.fastSimpleUUID();
        
        if (redisService.tryLock(lockKey, lockValue, expire, timeUnit)) {
            return lockValue;
        }
        return null;
    }
    
    /**
     * 尝试获取分布式锁（非阻塞）
     */
    public String tryLock(String key) {
        return tryLock(key, RedisConstant.DEFAULT_LOCK_EXPIRE, TimeUnit.SECONDS);
    }
    
    /**
     * 尝试获取分布式锁（带过期时间，非阻塞）
     */
    public String tryLock(String key, long expire, TimeUnit timeUnit) {
        String lockKey = buildLockKey(key);
        String lockValue = IdUtil.fastSimpleUUID();
        
        if (redisService.tryLock(lockKey, lockValue, expire, timeUnit)) {
            return lockValue;
        }
        return null;
    }
    
    /**
     * 尝试获取分布式锁（带等待时间）
     */
    public String tryLockWithWait(String key) {
        return tryLockWithWait(key, RedisConstant.DEFAULT_LOCK_WAIT, 
                              RedisConstant.DEFAULT_LOCK_EXPIRE, TimeUnit.SECONDS);
    }
    
    /**
     * 尝试获取分布式锁（带等待时间和过期时间）
     */
    public String tryLockWithWait(String key, long waitTime, long expireTime, TimeUnit timeUnit) {
        String lockKey = buildLockKey(key);
        String lockValue = IdUtil.fastSimpleUUID();
        
        if (redisService.tryLockWithWait(lockKey, lockValue, waitTime, expireTime, timeUnit)) {
            return lockValue;
        }
        return null;
    }
    
    /**
     * 释放分布式锁
     */
    public boolean unlock(String key, String lockValue) {
        String lockKey = buildLockKey(key);
        return redisService.unlock(lockKey, lockValue);
    }
    
    /**
     * 自动释放锁的模板方法
     */
    public <T> T executeWithLock(String key, LockCallback<T> callback) {
        return executeWithLock(key, RedisConstant.DEFAULT_LOCK_EXPIRE, TimeUnit.SECONDS, callback);
    }
    
    /**
     * 自动释放锁的模板方法（带过期时间）
     */
    public <T> T executeWithLock(String key, long expire, TimeUnit timeUnit, LockCallback<T> callback) {
        String lockValue = null;
        try {
            // 先尝试获取本地锁，避免重复创建锁对象
            localLock.lock();
            lockValue = lock(key, expire, timeUnit);
            if (StrUtil.isBlank(lockValue)) {
                throw new RuntimeException("获取分布式锁失败: " + key);
            }
            
            return callback.execute();
        } finally {
            if (lockValue != null) {
                unlock(key, lockValue);
            }
            localLock.unlock();
        }
    }
    
    /**
     * 重入锁（支持同一线程多次加锁）
     */
    public <T> T executeWithReentrantLock(String key, LockCallback<T> callback) {
        ThreadLocal<String> lockHolder = new ThreadLocal<>();
        
        String currentLockValue = lockHolder.get();
        if (currentLockValue != null) {
            // 当前线程已经持有锁，直接执行
            return callback.execute();
        }
        
        String lockValue = lock(key);
        if (StrUtil.isBlank(lockValue)) {
            throw new RuntimeException("获取分布式锁失败: " + key);
        }
        
        try {
            lockHolder.set(lockValue);
            return callback.execute();
        } finally {
            unlock(key, lockValue);
            lockHolder.remove();
        }
    }
    
    /**
     * 构建锁key
     */
    private String buildLockKey(String key) {
        return RedisConstant.LOCK_KEY_PREFIX + key;
    }
    
    /**
     * 锁回调接口
     */
    @FunctionalInterface
    public interface LockCallback<T> {
        T execute();
    }
}