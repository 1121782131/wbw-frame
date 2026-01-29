package com.wbw.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作服务接口
 */
public interface RedisService {
    
    // ============================= Key操作 =============================
    
    /**
     * 删除key
     */
    Boolean delete(String key);
    
    /**
     * 批量删除key
     */
    Long delete(List<String> keys);
    
    /**
     * 是否存在key
     */
    Boolean hasKey(String key);
    
    /**
     * 设置过期时间
     */
    Boolean expire(String key, long time, TimeUnit timeUnit);
    
    /**
     * 设置过期时间（秒）
     */
    Boolean expire(String key, long time);
    
    /**
     * 获取过期时间
     */
    Long getExpire(String key, TimeUnit timeUnit);
    
    /**
     * 获取过期时间（秒）
     */
    Long getExpire(String key);
    
    /**
     * 获取匹配的key
     */
    Set<String> keys(String pattern);
    
    // ============================= String操作 =============================
    
    /**
     * 设置值
     */
    void set(String key, Object value);
    
    /**
     * 设置值并设置过期时间
     */
    void set(String key, Object value, long time, TimeUnit timeUnit);
    
    /**
     * 设置值并设置过期时间（秒）
     */
    void set(String key, Object value, long time);
    
    /**
     * 设置值如果不存在
     */
    Boolean setIfAbsent(String key, Object value);
    
    /**
     * 设置值如果不存在并设置过期时间
     */
    Boolean setIfAbsent(String key, Object value, long time, TimeUnit timeUnit);
    
    /**
     * 获取值
     */
    <T> T get(String key);
    
    /**
     * 获取值并转换为指定类型
     */
    <T> T get(String key, Class<T> clazz);
    
    /**
     * 递增
     */
    Long increment(String key, long delta);
    
    /**
     * 递减
     */
    Long decrement(String key, long delta);
    
    // ============================= Hash操作 =============================
    
    /**
     * 设置Hash值
     */
    void hSet(String key, String hashKey, Object value);
    
    /**
     * 设置Hash值如果不存在
     */
    Boolean hSetIfAbsent(String key, String hashKey, Object value);
    
    /**
     * 获取Hash值
     */
    <T> T hGet(String key, String hashKey);
    
    /**
     * 获取Hash值并转换为指定类型
     */
    <T> T hGet(String key, String hashKey, Class<T> clazz);
    
    /**
     * 获取所有Hash值
     */
    Map<Object, Object> hGetAll(String key);
    
    /**
     * 批量设置Hash值
     */
    void hPutAll(String key, Map<String, Object> map);
    
    /**
     * 删除Hash值
     */
    Long hDelete(String key, Object... hashKeys);
    
    /**
     * 是否存在Hash键
     */
    Boolean hHasKey(String key, String hashKey);
    
    /**
     * Hash递增
     */
    Long hIncrement(String key, String hashKey, long delta);
    
    /**
     * Hash递减
     */
    Long hDecrement(String key, String hashKey, long delta);
    
    // ============================= List操作 =============================
    
    /**
     * 获取List
     */
    <T> List<T> lGet(String key, long start, long end);
    
    /**
     * 获取List大小
     */
    Long lSize(String key);
    
    /**
     * 获取List指定位置的值
     */
    <T> T lGetIndex(String key, long index);
    
    /**
     * 设置List指定位置的值
     */
    void lSet(String key, long index, Object value);
    
    /**
     * 移除List中的值
     */
    Long lRemove(String key, long count, Object value);
    
    /**
     * 从左边添加值
     */
    Long lLeftPush(String key, Object value);
    
    /**
     * 从左边批量添加值
     */
    Long lLeftPushAll(String key, Object... values);
    
    /**
     * 从右边添加值
     */
    Long lRightPush(String key, Object value);
    
    /**
     * 从右边批量添加值
     */
    Long lRightPushAll(String key, Object... values);
    
    /**
     * 从左边弹出值
     */
    <T> T lLeftPop(String key);
    
    /**
     * 从右边弹出值
     */
    <T> T lRightPop(String key);
    
    // ============================= Set操作 =============================
    
    /**
     * 添加Set值
     */
    Long sAdd(String key, Object... values);
    
    /**
     * 获取Set
     */
    <T> Set<T> sMembers(String key);
    
    /**
     * 是否是Set成员
     */
    Boolean sIsMember(String key, Object value);
    
    /**
     * 获取Set大小
     */
    Long sSize(String key);
    
    /**
     * 移除Set值
     */
    Long sRemove(String key, Object... values);
    
    /**
     * 随机获取Set值
     */
    <T> T sRandomMember(String key);
    
    /**
     * 随机获取Set多个值
     */
    <T> List<T> sRandomMembers(String key, long count);
    
    // ============================= ZSet操作 =============================
    
    /**
     * 添加ZSet值
     */
    Boolean zAdd(String key, Object value, double score);
    
    /**
     * 批量添加ZSet值
     */
    Long zAdd(String key, Set<Object> values);
    
    /**
     * 获取ZSet范围内的值
     */
    <T> Set<T> zRange(String key, long start, long end);
    
    /**
     * 获取ZSet范围内的值（按分数）
     */
    <T> Set<T> zRangeByScore(String key, double min, double max);
    
    /**
     * 获取ZSet大小
     */
    Long zSize(String key);
    
    /**
     * 获取ZSet分数
     */
    Double zScore(String key, Object value);
    
    /**
     * 移除ZSet值
     */
    Long zRemove(String key, Object... values);
    
    // ============================= 分布式锁 =============================
    
    /**
     * 尝试获取分布式锁
     */
    Boolean tryLock(String key, String value, long expireTime, TimeUnit timeUnit);
    
    /**
     * 尝试获取分布式锁（秒）
     */
    Boolean tryLock(String key, String value, long expireTime);
    
    /**
     * 释放分布式锁
     */
    Boolean unlock(String key, String value);
    
    /**
     * 尝试获取分布式锁（带等待时间）
     */
    Boolean tryLockWithWait(String key, String value, long waitTime, long expireTime, TimeUnit timeUnit);
    
    // ============================= 批量操作 =============================
    
    /**
     * 批量获取值
     */
    <T> List<T> multiGet(List<String> keys);
    
    /**
     * 批量设置值
     */
    void multiSet(Map<String, Object> map);
    
    /**
     * 批量设置值如果不存在
     */
    Boolean multiSetIfAbsent(Map<String, Object> map);
    
    // ============================= 其他操作 =============================
    
    /**
     * 获取RedisTemplate
     */
    Object getRedisTemplate();
    
    /**
     * 获取StringRedisTemplate
     */
    Object getStringRedisTemplate();
}