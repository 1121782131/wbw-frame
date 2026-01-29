# wbw-redis 模块

基于Spring Boot 3.x的Redis配置和工具模块，提供完整的Redis操作封装和分布式锁功能。

## 特性

- ✅ 完整的Redis配置（单机、哨兵、集群）
- ✅ 连接池优化配置
- ✅ Fastjson2序列化支持
- ✅ 完整的Redis操作封装
- ✅ 分布式锁实现
- ✅ Redis Key工具类
- ✅ 自动配置支持
- ✅ 异常统一处理

## 快速开始

### 1. 引入依赖

在pom.xml中添加：

```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-redis</artifactId>
</dependency>
```

### 2. 配置文件

在application.yml文件中添加以下配置：

```yaml
# Redis配置
wbw:
  redis:
    host: localhost
    port: 6379
    password: 123456
    database: 0
    timeout: 2000ms
    pool:
      max-active: 8
      max-idle: 8
      min-idle: 0
    key-prefix: "wbw:"
    default-expire: 86400  # 24小时
```

### 3. 使用示例

#### 3.1 基本使用

```java
@Service
public class UserService {
    
    @Autowired
    private RedisService redisService;
    
    @Autowired
    private RedisLock redisLock;
    
    public User getUserById(String userId) {
        String key = RedisKeyBuilder.buildUserKey(userId);
        User user = redisService.get(key, User.class);
        
        if (user == null) {
            user = userDao.findById(userId);
            if (user != null) {
                redisService.set(key, user, RedisConstant.ONE_HOUR);
            }
        }
        return user;
    }
    
    public void updateUser(User user) {
        String lockKey = "user:" + user.getId();
        String lockValue = redisLock.lock(lockKey);
        
        try {
            // 执行业务逻辑
            userDao.update(user);
            
            // 更新缓存
            String key = RedisKeyBuilder.buildUserKey(user.getId());
            redisService.set(key, user, RedisConstant.ONE_HOUR);
        } finally {
            redisLock.unlock(lockKey, lockValue);
        }
    }
}
```

#### 3.2 分布式锁使用

```java
@Autowired
private RedisLock redisLock;

// 简单锁
public void processWithLock() {
    String lockValue = redisLock.lock("process:lock");
    if (lockValue != null) {
        try {
            // 执行业务逻辑
        } finally {
            redisLock.unlock("process:lock", lockValue);
        }
    }
}

// 带等待的锁
public void processWithWaitLock() {
    String lockValue = redisLock.tryLockWithWait("process:lock", 10, 30, TimeUnit.SECONDS);
    if (lockValue != null) {
        try {
            // 执行业务逻辑
        } finally {
            redisLock.unlock("process:lock", lockValue);
        }
    }
}

// 模板方法
public void processWithTemplate() {
    redisLock.executeWithLock("process:lock", () -> {
        // 执行业务逻辑
        return null;
    });
}
```

## 配置说明

### 核心配置项

| 配置参数名称 | 数据类型 | 默认值 | 详细功能描述 |
| --- | --- | --- | --- |
| `wbw.redis.host` | String | localhost | Redis服务器地址 |
| `wbw.redis.port` | int | 6379 | Redis服务器端口 |
| `wbw.redis.password` | String | "" | Redis密码 |
| `wbw.redis.database` | int | 0 | Redis数据库索引 |
| `wbw.redis.timeout` | Duration | 2000ms | 连接超时时间 |
| `wbw.redis.pool.max-active` | int | 8 | 连接池最大连接数 |
| `wbw.redis.pool.max-idle` | int | 8 | 连接池最大空闲连接数 |
| `wbw.redis.pool.min-idle` | int | 0 | 连接池最小空闲连接数 |
| `wbw.redis.key-prefix` | String | "wbw:" | 键前缀 |
| `wbw.redis.default-expire` | int | 86400 | 默认过期时间（秒） |

### 不同部署模式配置

#### 单机模式配置

```yaml
# 单机模式配置
wbw:
  redis:
    host: 127.0.0.1
    port: 6379
    password: your_password
    database: 0
```

#### 哨兵模式配置

```yaml
# 哨兵模式配置
wbw:
  redis:
    sentinel:
      master: mymaster
      nodes: 127.0.0.1:26379,127.0.0.1:26380
    password: your_password
    database: 0
```

#### 集群模式配置

```yaml
# 集群模式配置
wbw:
  redis:
    cluster:
      nodes: 127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002
    password: your_password
```

## 最简化可运行配置

```yaml
# 最简化配置
wbw:
  redis:
    host: localhost
    port: 6379
```

## 不同配置组合下的行为差异

| 配置组合 | 行为差异 |
| --- | --- |
| 单机模式 | 使用单个Redis实例，适用于开发和测试环境 |
| 哨兵模式 | 支持Redis主从复制和自动故障转移，适用于生产环境 |
| 集群模式 | 支持Redis集群，提供高可用性和水平扩展能力，适用于大规模生产环境 |
| 带密码配置 | 启用Redis认证，提高安全性 |
| 连接池配置 | 优化连接管理，提高性能 |
| 键前缀配置 | 避免键冲突，便于管理 |

## 常见问题

### 1. Redis连接失败

**症状**：应用无法连接到Redis服务器。

**解决方案**：
- 检查Redis服务是否正常运行
- 检查网络连接是否畅通
- 检查配置文件中的主机地址和端口是否正确
- 检查Redis密码是否正确

### 2. 分布式锁不释放

**症状**：获取锁后，由于异常导致锁未释放。

**解决方案**：
- 使用try-finally块确保锁的释放
- 设置合理的锁过期时间
- 使用RedisLock的模板方法，自动处理锁的获取和释放

### 3. 缓存穿透

**症状**：查询不存在的数据，导致每次都访问数据库。

**解决方案**：
- 对不存在的数据设置空值缓存
- 使用布隆过滤器过滤不存在的键

### 4. 缓存雪崩

**症状**：大量缓存同时过期，导致数据库压力骤增。

**解决方案**：
- 设置随机过期时间
- 使用分层缓存
- 实现缓存预热

## 最佳实践

1. **合理设置过期时间**：根据业务场景设置合适的缓存过期时间
2. **使用键前缀**：避免不同业务的键冲突
3. **实现缓存一致性**：确保缓存与数据库数据的一致性
4. **使用分布式锁**：在并发场景下保证数据一致性
5. **监控Redis性能**：定期监控Redis的内存使用、连接数等指标
6. **合理使用数据结构**：根据业务场景选择合适的Redis数据结构
7. **避免大键**：避免存储过大的数据，影响Redis性能
8. **实现缓存降级**：当Redis不可用时，优雅降级到数据库