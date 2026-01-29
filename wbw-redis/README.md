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


单机模式配置：
wbw:
  redis:
    host: 127.0.0.1
    port: 6379
    password: your_password
    database: 0


哨兵模式配置：：
wbw:
  redis:
    sentinel:
      master: mymaster
      nodes: 127.0.0.1:26379,127.0.0.1:26380
    password: your_password
    database: 0


集群模式配置：
wbw:
  redis:
    cluster:
      nodes: 127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002
    password: your_password