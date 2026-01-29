package com.wbw.redis.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wbw.redis.service.RedisService;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisServiceImpl implements RedisService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate,
                           StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    
    @Override
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RuntimeException("删除key失败: " + key, e);
        }
    }
    
    @Override
    public Long delete(List<String> keys) {
        try {
            return redisTemplate.delete(keys);
        } catch (Exception e) {
            throw new RuntimeException("批量删除key失败", e);
        }
    }
    
    @Override
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new RuntimeException("检查key是否存在失败: " + key, e);
        }
    }
    
    @Override
    public Boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                return redisTemplate.expire(key, time, timeUnit);
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("设置过期时间失败: " + key, e);
        }
    }
    
    @Override
    public Boolean expire(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }
    
    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            return redisTemplate.getExpire(key, timeUnit);
        } catch (Exception e) {
            throw new RuntimeException("获取过期时间失败: " + key, e);
        }
    }
    
    @Override
    public Long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }
    
    @Override
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            throw new RuntimeException("获取匹配key失败: " + pattern, e);
        }
    }
    
    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new RuntimeException("设置值失败: " + key, e);
        }
    }
    
    @Override
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("设置值失败: " + key, e);
        }
    }
    
    @Override
    public void set(String key, Object value, long time) {
        set(key, value, time, TimeUnit.SECONDS);
    }
    
    @Override
    public Boolean setIfAbsent(String key, Object value) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            throw new RuntimeException("设置值如果不存在失败: " + key, e);
        }
    }
    
    @Override
    public Boolean setIfAbsent(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                return redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
            }
            return setIfAbsent(key, value);
        } catch (Exception e) {
            throw new RuntimeException("设置值如果不存在失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return (T) operations.get(key);
        } catch (Exception e) {
            throw new RuntimeException("获取值失败: " + key, e);
        }
    }
    
    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            return clazz.cast(value);
        } catch (Exception e) {
            throw new RuntimeException("获取值失败: " + key, e);
        }
    }
    
    @Override
    public Long increment(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            throw new RuntimeException("递增失败: " + key, e);
        }
    }
    
    @Override
    public Long decrement(String key, long delta) {
        try {
            return redisTemplate.opsForValue().decrement(key, delta);
        } catch (Exception e) {
            throw new RuntimeException("递减失败: " + key, e);
        }
    }
    
    @Override
    public void hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            throw new RuntimeException("设置Hash值失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    public Boolean hSetIfAbsent(String key, String hashKey, Object value) {
        try {
            return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
        } catch (Exception e) {
            throw new RuntimeException("设置Hash值如果不存在失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String hashKey) {
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return (T) operations.get(key, hashKey);
        } catch (Exception e) {
            throw new RuntimeException("获取Hash值失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    public <T> T hGet(String key, String hashKey, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForHash().get(key, hashKey);
            if (value == null) {
                return null;
            }
            return clazz.cast(value);
        } catch (Exception e) {
            throw new RuntimeException("获取Hash值失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    public Map<Object, Object> hGetAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            throw new RuntimeException("获取所有Hash值失败: " + key, e);
        }
    }
    
    @Override
    public void hPutAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            throw new RuntimeException("批量设置Hash值失败: " + key, e);
        }
    }
    
    @Override
    public Long hDelete(String key, Object... hashKeys) {
        try {
            return redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            throw new RuntimeException("删除Hash值失败: " + key, e);
        }
    }
    
    @Override
    public Boolean hHasKey(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            throw new RuntimeException("检查Hash键是否存在失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    public Long hIncrement(String key, String hashKey, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {
            throw new RuntimeException("Hash递增失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    public Long hDecrement(String key, String hashKey, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, -delta);
        } catch (Exception e) {
            throw new RuntimeException("Hash递减失败: " + key + "->" + hashKey, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> lGet(String key, long start, long end) {
        try {
            return (List<T>) redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException("获取List失败: " + key, e);
        }
    }
    
    @Override
    public Long lSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            throw new RuntimeException("获取List大小失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T lGetIndex(String key, long index) {
        try {
            return (T) redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            throw new RuntimeException("获取List指定位置值失败: " + key, e);
        }
    }
    
    @Override
    public void lSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
        } catch (Exception e) {
            throw new RuntimeException("设置List指定位置值失败: " + key, e);
        }
    }
    
    @Override
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            throw new RuntimeException("移除List值失败: " + key, e);
        }
    }
    
    @Override
    public Long lLeftPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            throw new RuntimeException("从左边添加值失败: " + key, e);
        }
    }
    
    @Override
    public Long lLeftPushAll(String key, Object... values) {
        try {
            return redisTemplate.opsForList().leftPushAll(key, values);
        } catch (Exception e) {
            throw new RuntimeException("从左边批量添加值失败: " + key, e);
        }
    }
    
    @Override
    public Long lRightPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            throw new RuntimeException("从右边添加值失败: " + key, e);
        }
    }
    
    @Override
    public Long lRightPushAll(String key, Object... values) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, values);
        } catch (Exception e) {
            throw new RuntimeException("从右边批量添加值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T lLeftPop(String key) {
        try {
            return (T) redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            throw new RuntimeException("从左边弹出值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T lRightPop(String key) {
        try {
            return (T) redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            throw new RuntimeException("从右边弹出值失败: " + key, e);
        }
    }
    
    @Override
    public Long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            throw new RuntimeException("添加Set值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sMembers(String key) {
        try {
            return (Set<T>) redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            throw new RuntimeException("获取Set失败: " + key, e);
        }
    }
    
    @Override
    public Boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            throw new RuntimeException("检查Set成员失败: " + key, e);
        }
    }
    
    @Override
    public Long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            throw new RuntimeException("获取Set大小失败: " + key, e);
        }
    }
    
    @Override
    public Long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            throw new RuntimeException("移除Set值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T sRandomMember(String key) {
        try {
            return (T) redisTemplate.opsForSet().randomMember(key);
        } catch (Exception e) {
            throw new RuntimeException("随机获取Set值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> sRandomMembers(String key, long count) {
        try {
            return (List<T>) redisTemplate.opsForSet().randomMembers(key, count);
        } catch (Exception e) {
            throw new RuntimeException("随机获取Set多个值失败: " + key, e);
        }
    }
    
    @Override
    public Boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            throw new RuntimeException("添加ZSet值失败: " + key, e);
        }
    }
    
    @Override
    public Long zAdd(String key, Set<Object> values) {
        try {
            Set<ZSetOperations.TypedTuple<Object>> tuples = values.stream()
                .map(value -> ZSetOperations.TypedTuple.of(value, (double) System.currentTimeMillis()))
                .collect(Collectors.toSet());
            return redisTemplate.opsForZSet().add(key, tuples);
        } catch (Exception e) {
            throw new RuntimeException("批量添加ZSet值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zRange(String key, long start, long end) {
        try {
            return (Set<T>) redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException("获取ZSet范围内的值失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zRangeByScore(String key, double min, double max) {
        try {
            return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            throw new RuntimeException("获取ZSet范围内的值（按分数）失败: " + key, e);
        }
    }
    
    @Override
    public Long zSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            throw new RuntimeException("获取ZSet大小失败: " + key, e);
        }
    }
    
    @Override
    public Double zScore(String key, Object value) {
        try {
            return redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            throw new RuntimeException("获取ZSet分数失败: " + key, e);
        }
    }
    
    @Override
    public Long zRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            throw new RuntimeException("移除ZSet值失败: " + key, e);
        }
    }
    
    @Override
    public Boolean tryLock(String key, String value, long expireTime, TimeUnit timeUnit) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, timeUnit);
        } catch (Exception e) {
            throw new RuntimeException("获取分布式锁失败: " + key, e);
        }
    }
    
    @Override
    public Boolean tryLock(String key, String value, long expireTime) {
        return tryLock(key, value, expireTime, TimeUnit.SECONDS);
    }
    
    @Override
    public Boolean unlock(String key, String value) {
        try {
            String currentValue = stringRedisTemplate.opsForValue().get(key);
            if (StrUtil.equals(value, currentValue)) {
                return redisTemplate.delete(key);
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("释放分布式锁失败: " + key, e);
        }
    }
    
    @Override
    public Boolean tryLockWithWait(String key, String value, long waitTime, long expireTime, TimeUnit timeUnit) {
        try {
            long start = System.currentTimeMillis();
            while (true) {
                if (tryLock(key, value, expireTime, timeUnit)) {
                    return true;
                }
                long end = System.currentTimeMillis();
                if (end - start > timeUnit.toMillis(waitTime)) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("等待获取分布式锁失败: " + key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> multiGet(List<String> keys) {
        try {
            return (List<T>) redisTemplate.opsForValue().multiGet(keys);
        } catch (Exception e) {
            throw new RuntimeException("批量获取值失败", e);
        }
    }
    
    @Override
    public void multiSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
        } catch (Exception e) {
            throw new RuntimeException("批量设置值失败", e);
        }
    }
    
    @Override
    public Boolean multiSetIfAbsent(Map<String, Object> map) {
        try {
            return redisTemplate.opsForValue().multiSetIfAbsent(map);
        } catch (Exception e) {
            throw new RuntimeException("批量设置值如果不存在失败", e);
        }
    }
    
    @Override
    public Object getRedisTemplate() {
        return redisTemplate;
    }
    
    @Override
    public Object getStringRedisTemplate() {
        return stringRedisTemplate;
    }
}