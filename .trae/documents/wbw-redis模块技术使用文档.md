# wbw-redis 模块技术使用文档

## 1. 模块概述

`wbw-redis` 模块是企业级微服务框架的缓存模块，基于 Redis 实现了完整的缓存操作和分布式锁功能。该模块设计目标是为微服务架构提供高性能、可靠的缓存解决方案，支持各种缓存场景和分布式锁需求。

**核心功能：**
- Redis 缓存操作（String、Hash、List、Set、ZSet）
- 分布式锁实现
- 批量操作支持
- 缓存键管理
- 连接池优化

**应用场景：**
- 热点数据缓存
- 分布式锁
- 计数器
- 消息队列
- 会话管理

## 2. 接口说明

### 2.1 Redis 操作服务接口

#### 2.1.1 Key 操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.delete(String key)` | 删除指定键 |
| `RedisService.delete(List<String> keys)` | 批量删除键 |
| `RedisService.hasKey(String key)` | 检查键是否存在 |
| `RedisService.expire(String key, long time, TimeUnit timeUnit)` | 设置键过期时间 |
| `RedisService.expire(String key, long time)` | 设置键过期时间（秒） |
| `RedisService.getExpire(String key, TimeUnit timeUnit)` | 获取键过期时间 |
| `RedisService.getExpire(String key)` | 获取键过期时间（秒） |
| `RedisService.keys(String pattern)` | 获取匹配的键集合 |

#### 2.1.2 String 操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.set(String key, Object value)` | 设置字符串值 |
| `RedisService.set(String key, Object value, long time, TimeUnit timeUnit)` | 设置字符串值并设置过期时间 |
| `RedisService.set(String key, Object value, long time)` | 设置字符串值并设置过期时间（秒） |
| `RedisService.setIfAbsent(String key, Object value)` | 仅当键不存在时设置值 |
| `RedisService.setIfAbsent(String key, Object value, long time, TimeUnit timeUnit)` | 仅当键不存在时设置值并设置过期时间 |
| `RedisService.get(String key)` | 获取字符串值 |
| `RedisService.get(String key, Class<T> clazz)` | 获取字符串值并转换为指定类型 |
| `RedisService.increment(String key, long delta)` | 递增操作 |
| `RedisService.decrement(String key, long delta)` | 递减操作 |

#### 2.1.3 Hash 操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.hSet(String key, String hashKey, Object value)` | 设置哈希表字段值 |
| `RedisService.hSetIfAbsent(String key, String hashKey, Object value)` | 仅当哈希表字段不存在时设置值 |
| `RedisService.hGet(String key, String hashKey)` | 获取哈希表字段值 |
| `RedisService.hGet(String key, String hashKey, Class<T> clazz)` | 获取哈希表字段值并转换为指定类型 |
| `RedisService.hGetAll(String key)` | 获取哈希表所有字段值 |
| `RedisService.hPutAll(String key, Map<String, Object> map)` | 批量设置哈希表字段值 |
| `RedisService.hDelete(String key, Object... hashKeys)` | 删除哈希表字段 |
| `RedisService.hHasKey(String key, String hashKey)` | 检查哈希表字段是否存在 |
| `RedisService.hIncrement(String key, String hashKey, long delta)` | 哈希表字段递增 |
| `RedisService.hDecrement(String key, String hashKey, long delta)` | 哈希表字段递减 |

#### 2.1.4 List 操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.lGet(String key, long start, long end)` | 获取列表指定范围元素 |
| `RedisService.lSize(String key)` | 获取列表大小 |
| `RedisService.lGetIndex(String key, long index)` | 获取列表指定位置元素 |
| `RedisService.lSet(String key, long index, Object value)` | 设置列表指定位置元素 |
| `RedisService.lRemove(String key, long count, Object value)` | 移除列表中指定值 |
| `RedisService.lLeftPush(String key, Object value)` | 从列表左侧添加元素 |
| `RedisService.lLeftPushAll(String key, Object... values)` | 从列表左侧批量添加元素 |
| `RedisService.lRightPush(String key, Object value)` | 从列表右侧添加元素 |
| `RedisService.lRightPushAll(String key, Object... values)` | 从列表右侧批量添加元素 |
| `RedisService.lLeftPop(String key)` | 从列表左侧弹出元素 |
| `RedisService.lRightPop(String key)` | 从列表右侧弹出元素 |

#### 2.1.5 Set 操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.sAdd(String key, Object... values)` | 向集合添加元素 |
| `RedisService.sMembers(String key)` | 获取集合所有元素 |
| `RedisService.sIsMember(String key, Object value)` | 检查元素是否在集合中 |
| `RedisService.sSize(String key)` | 获取集合大小 |
| `RedisService.sRemove(String key, Object... values)` | 从集合移除元素 |
| `RedisService.sRandomMember(String key)` | 随机获取集合元素 |
| `RedisService.sRandomMembers(String key, long count)` | 随机获取多个集合元素 |

#### 2.1.6 ZSet 操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.zAdd(String key, Object value, double score)` | 向有序集合添加元素 |
| `RedisService.zAdd(String key, Set<Object> values)` | 批量向有序集合添加元素 |
| `RedisService.zRange(String key, long start, long end)` | 获取有序集合指定范围元素 |
| `RedisService.zRangeByScore(String key, double min, double max)` | 根据分数范围获取有序集合元素 |
| `RedisService.zSize(String key)` | 获取有序集合大小 |
| `RedisService.zScore(String key, Object value)` | 获取有序集合元素分数 |
| `RedisService.zRemove(String key, Object... values)` | 从有序集合移除元素 |

#### 2.1.7 分布式锁操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.tryLock(String key, String value, long expireTime, TimeUnit timeUnit)` | 尝试获取分布式锁 |
| `RedisService.tryLock(String key, String value, long expireTime)` | 尝试获取分布式锁（秒） |
| `RedisService.unlock(String key, String value)` | 释放分布式锁 |
| `RedisService.tryLockWithWait(String key, String value, long waitTime, long expireTime, TimeUnit timeUnit)` | 尝试获取分布式锁（带等待时间） |

#### 2.1.8 批量操作

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisService.multiGet(List<String> keys)` | 批量获取值 |
| `RedisService.multiSet(Map<String, Object> map)` | 批量设置值 |
| `RedisService.multiSetIfAbsent(Map<String, Object> map)` | 批量设置值（仅当键不存在） |

### 2.2 Redis 锁工具接口

| 接口名称 | 功能描述 |
|---------|--------|
| `RedisLock.tryLock()` | 尝试获取锁 |
| `RedisLock.tryLock(long waitTime, TimeUnit timeUnit)` | 尝试获取锁（带等待时间） |
| `RedisLock.unlock()` | 释放锁 |
| `RedisLock.isLocked()` | 检查锁是否被持有 |

## 3. 参数规范

### 3.1 RedisService 方法参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 描述 |
|---------|------|--------|---------|------|
| `key` | String | 是 | 非空字符串 | Redis 键 |
| `hashKey` | String | 是（Hash操作） | 非空字符串 | 哈希表字段键 |
| `value` | Object | 是 | 任意可序列化对象 | 值 |
| `time` | long | 否 | 正数 | 过期时间 |
| `timeUnit` | TimeUnit | 否 | 时间单位枚举 | 时间单位 |
| `delta` | long | 是（递增/递减） | 任意整数 | 递增量 |
| `start` | long | 是（范围操作） | 非负整数 | 起始位置 |
| `end` | long | 是（范围操作） | 非负整数 | 结束位置 |
| `count` | long | 是（计数操作） | 正数 | 数量 |
| `score` | double | 是（ZSet操作） | 任意浮点数 | 分数 |
| `min` | double | 是（ZSet范围操作） | 任意浮点数 | 最小分数 |
| `max` | double | 是（ZSet范围操作） | 任意浮点数 | 最大分数 |
| `keys` | List<String> | 是（批量操作） | 非空列表 | 键列表 |
| `values` | Object... | 是（批量操作） | 任意对象数组 | 值数组 |
| `map` | Map<String, Object> | 是（批量操作） | 非空映射 | 键值对映射 |
| `clazz` | Class<T> | 是（类型转换） | 任意类 | 目标类型 |

### 3.2 分布式锁参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 描述 |
|---------|------|--------|---------|------|
| `key` | String | 是 | 非空字符串 | 锁键 |
| `value` | String | 是 | 非空字符串 | 锁值（用于解锁时验证） |
| `expireTime` | long | 是 | 正数 | 锁过期时间 |
| `waitTime` | long | 是（带等待） | 正数 | 等待时间 |
| `timeUnit` | TimeUnit | 否 | 时间单位枚举 | 时间单位 |

## 4. 返回值定义

### 4.1 基本操作返回值

| 操作类型 | 返回类型 | 含义 |
|---------|---------|------|
| `delete` | Boolean/Long | 操作结果/删除数量 |
| `hasKey` | Boolean | 是否存在 |
| `expire` | Boolean | 是否设置成功 |
| `getExpire` | Long | 过期时间 |
| `keys` | Set<String> | 键集合 |
| `set` | void | 无返回值 |
| `setIfAbsent` | Boolean | 是否设置成功 |
| `get` | T | 获取的值 |
| `increment/decrement` | Long | 操作后的值 |
| `hSet` | void | 无返回值 |
| `hSetIfAbsent` | Boolean | 是否设置成功 |
| `hGet` | T | 获取的值 |
| `hGetAll` | Map<Object, Object> | 所有字段值 |
| `hPutAll` | void | 无返回值 |
| `hDelete` | Long | 删除数量 |
| `hHasKey` | Boolean | 是否存在 |
| `hIncrement/hDecrement` | Long | 操作后的值 |
| `lGet` | List<T> | 元素列表 |
| `lSize` | Long | 列表大小 |
| `lGetIndex` | T | 元素值 |
| `lSet` | void | 无返回值 |
| `lRemove` | Long | 移除数量 |
| `lLeftPush/lRightPush` | Long | 操作后列表大小 |
| `lLeftPushAll/lRightPushAll` | Long | 操作后列表大小 |
| `lLeftPop/lRightPop` | T | 弹出的元素 |
| `sAdd` | Long | 添加数量 |
| `sMembers` | Set<T> | 集合元素 |
| `sIsMember` | Boolean | 是否是成员 |
| `sSize` | Long | 集合大小 |
| `sRemove` | Long | 移除数量 |
| `sRandomMember` | T | 随机元素 |
| `sRandomMembers` | List<T> | 随机元素列表 |
| `zAdd` | Boolean/Long | 是否添加成功/添加数量 |
| `zRange/zRangeByScore` | Set<T> | 元素集合 |
| `zSize` | Long | 集合大小 |
| `zScore` | Double | 元素分数 |
| `zRemove` | Long | 移除数量 |
| `tryLock/tryLockWithWait` | Boolean | 是否获取锁成功 |
| `unlock` | Boolean | 是否释放锁成功 |
| `multiGet` | List<T> | 值列表 |
| `multiSet` | void | 无返回值 |
| `multiSetIfAbsent` | Boolean | 是否设置成功 |

### 4.2 RedisLock 返回值

| 方法名称 | 返回类型 | 含义 |
|---------|---------|------|
| `tryLock` | Boolean | 是否获取锁成功 |
| `isLocked` | Boolean | 锁是否被持有 |
| `unlock` | void | 无返回值 |

## 5. 使用示例

### 5.1 基本缓存操作示例

```java
// 1. 注入Redis服务
@Autowired
private RedisService redisService;

// 2. String 操作
// 设置值
redisService.set("user:1", user);
// 设置值并过期
redisService.set("user:1", user, 3600);
// 获取值
User user = redisService.get("user:1", User.class);
// 递增
Long count = redisService.increment("counter", 1);

// 3. Hash 操作
// 设置哈希值
redisService.hSet("user:info:1", "name", "张三");
redisService.hSet("user:info:1", "age", 25);
// 获取哈希值
String name = redisService.hGet("user:info:1", "name", String.class);
Integer age = redisService.hGet("user:info:1", "age", Integer.class);
// 获取所有哈希值
Map<Object, Object> userInfo = redisService.hGetAll("user:info:1");

// 4. List 操作
// 添加元素
redisService.lRightPush("message:queue", "消息1");
redisService.lRightPush("message:queue", "消息2");
// 获取元素
List<String> messages = redisService.lGet("message:queue", 0, -1);
// 弹出元素
String message = redisService.lLeftPop("message:queue", String.class);

// 5. Set 操作
// 添加元素
redisService.sAdd("user:tags:1", "tag1", "tag2", "tag3");
// 获取元素
Set<String> tags = redisService.sMembers("user:tags:1", String.class);
// 检查元素
boolean hasTag = redisService.sIsMember("user:tags:1", "tag1");

// 6. ZSet 操作
// 添加元素
redisService.zAdd("user:ranking", "user1", 100);
redisService.zAdd("user:ranking", "user2", 90);
// 获取元素
Set<String> topUsers = redisService.zRange("user:ranking", 0, 9);
```

### 5.2 分布式锁使用示例

```java
// 1. 简单分布式锁
String lockKey = "order:lock:" + orderId;
String lockValue = UUID.randomUUID().toString();
try {
    // 尝试获取锁，过期时间30秒
    boolean locked = redisService.tryLock(lockKey, lockValue, 30);
    if (locked) {
        // 执行业务逻辑
        createOrder(orderId);
    } else {
        throw new BusinessException("系统繁忙，请稍后再试");
    }
} finally {
    // 释放锁
    redisService.unlock(lockKey, lockValue);
}

// 2. 带等待时间的分布式锁
String lockKey = "inventory:lock:" + productId;
String lockValue = UUID.randomUUID().toString();
try {
    // 尝试获取锁，最多等待5秒，过期时间30秒
    boolean locked = redisService.tryLockWithWait(lockKey, lockValue, 5, 30, TimeUnit.SECONDS);
    if (locked) {
        // 执行业务逻辑
        reduceInventory(productId, quantity);
    } else {
        throw new BusinessException("系统繁忙，请稍后再试");
    }
} finally {
    // 释放锁
    redisService.unlock(lockKey, lockValue);
}

// 3. 使用RedisLock工具类
RedisLock lock = new RedisLock(redisService, "resource:lock", 30, TimeUnit.SECONDS);
try {
    if (lock.tryLock()) {
        // 执行业务逻辑
        processResource();
    } else {
        throw new BusinessException("资源被占用，请稍后再试");
    }
} finally {
    lock.unlock();
}
```

### 5.3 批量操作示例

```java
// 批量设置
Map<String, Object> data = new HashMap<>();
data.put("user:1", user1);
data.put("user:2", user2);
data.put("user:3", user3);
redisService.multiSet(data);

// 批量获取
List<String> keys = Arrays.asList("user:1", "user:2", "user:3");
List<User> users = redisService.multiGet(keys);

// 批量设置（仅当键不存在）
Map<String, Object> newData = new HashMap<>();
newData.put("user:4", user4);
newData.put("user:5", user5);
boolean success = redisService.multiSetIfAbsent(newData);
```

## 6. 注意事项

### 6.1 性能考量

- **连接池配置**：应根据系统负载合理配置Redis连接池大小，避免连接过多或过少。
- **批量操作**：对于大量数据操作，应使用批量操作API，减少网络往返次数。
- **键设计**：合理设计Redis键名，使用前缀和命名空间，便于管理和查询。
- **过期时间**：为缓存数据设置合理的过期时间，避免内存占用过大。
- **序列化**：选择高效的序列化方式，如JSON或Protobuf，减少网络传输和存储开销。

### 6.2 使用限制

- **数据大小**：单个Redis键值大小不应超过512MB，建议控制在合理范围内。
- **命令复杂度**：避免使用复杂度为O(N)的命令操作大型集合，如KEYS、HGETALL等。
- **事务支持**：Redis事务是乐观锁，不支持回滚，应注意业务逻辑的正确性。
- **分布式锁**：分布式锁应设置合理的过期时间，避免死锁。解锁时应验证锁值，避免误解锁。

### 6.3 常见问题解决方案

| 问题 | 原因 | 解决方案 |
|------|------|--------|
| Redis连接失败 | 网络问题或Redis服务不可用 | 检查网络连接和Redis服务状态，配置合理的连接超时和重试机制 |
| 数据序列化失败 | 对象未实现Serializable接口或序列化方式不支持 | 确保对象可序列化，选择合适的序列化方式 |
| 分布式锁死锁 | 锁未正确释放或过期时间设置不合理 | 确保在finally块中释放锁，设置合理的过期时间 |
| 内存占用过高 | 缓存数据过多或过期时间过长 | 清理过期数据，设置合理的缓存策略，使用Redis的内存淘汰机制 |
| 性能下降 | 命令执行时间过长或连接池耗尽 | 优化命令执行，合理配置连接池，使用管道和批量操作 |

### 6.4 最佳实践

- **键命名规范**：使用`{业务}:{类型}:{id}`的格式，如`user:info:1`。
- **过期时间**：根据业务需求设置合理的过期时间，热点数据可适当延长。
- **分布式锁**：使用UUID作为锁值，确保解锁时的安全性。
- **异常处理**：捕获Redis操作异常，避免影响业务流程。
- **监控告警**：监控Redis的使用率、命中率、响应时间等指标，设置合理的告警阈值。
- **测试覆盖**：为核心缓存操作编写单元测试，确保功能稳定性。
- **配置管理**：通过配置中心管理Redis连接参数，支持不同环境的灵活调整。