package com.wbw.security.service.impl;

import com.wbw.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Token清理服务
 * 定期清理黑名单中的过期token
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    
    private final RedisService redisService;
    
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
    /**
     * 定期清理过期的token黑名单
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        try {
            log.debug("开始清理过期的token黑名单");
            
            // 获取所有黑名单中的token
            Set<String> keys = redisService.keys(TOKEN_BLACKLIST_PREFIX + "*");
            
            if (keys == null || keys.isEmpty()) {
                log.debug("没有需要清理的token黑名单");
                return;
            }
            
            int cleanupCount = 0;
            for (String key : keys) {
                // 检查key是否过期
                Long expire = redisService.getExpire(key);
                if (expire == null || expire <= 0) {
                    // 如果key已过期或没有设置过期时间，删除它
                    redisService.delete(key);
                    cleanupCount++;
                }
            }
            
            log.debug("清理过期的token黑名单完成，共清理 {} 个token", cleanupCount);
        } catch (Exception e) {
            log.error("清理过期的token黑名单失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 手动触发清理过期的token黑名单
     */
    public void triggerCleanup() {
        cleanupExpiredTokens();
    }
}