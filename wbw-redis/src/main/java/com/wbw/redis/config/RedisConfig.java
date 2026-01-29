package com.wbw.redis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis自动配置类
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    /**
     * Jedis连接工厂配置
     */
    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        config.setDatabase(redisProperties.getDatabase());
        
        if (redisProperties.getPassword() != null && !redisProperties.getPassword().isEmpty()) {
            config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = 
            JedisClientConfiguration.builder()
                .connectTimeout(redisProperties.getTimeout())
                .readTimeout(redisProperties.getTimeout());
        
        // 配置连接池
        builder.usePooling().poolConfig(jedisPoolConfig());
        
        return new JedisConnectionFactory(config, builder.build());
    }

    /**
     * Jedis连接池配置
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        RedisProperties.Pool pool = redisProperties.getPool();
        
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        
        if (pool.getMaxWait() != null) {
            poolConfig.setMaxWait(pool.getMaxWait());
        }
        
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
        poolConfig.setNumTestsPerEvictionRun(3);
        
        return poolConfig;
    }

    /**
     * Redis缓存管理器
     */
    @Bean
    @ConditionalOnProperty(name = "wbw.redis.cache-enabled", havingValue = "true", matchIfMissing = true)
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(redisProperties.getDefaultExpire()))
            .disableCachingNullValues()
            .computePrefixWith(cacheName -> 
                redisProperties.isEnableKeyPrefix() ? 
                redisProperties.getKeyPrefix() + cacheName + ":" : 
                cacheName + ":");
        
        // 特殊缓存的过期时间配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        // 可以在这里配置特定的缓存过期时间
        // cacheConfigurations.put("users", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .transactionAware()
            .build();
    }
}