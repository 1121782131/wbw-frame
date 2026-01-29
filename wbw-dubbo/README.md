# Dubbo配置方案

## 1. 模块介绍

`wbw-dubbo` 模块是基于 Apache Dubbo 3.2.0 实现的服务调用模块，提供了完整的 Dubbo 配置方案，支持服务注册与发现、负载均衡、服务降级与容错等核心功能。

## 2. 依赖管理

### 2.1 核心依赖

- `dubbo-spring-boot-starter`：Dubbo Spring Boot 启动器
- `dubbo-registry-nacos`：Dubbo Nacos 注册中心集成
- `spring-boot-starter`：Spring Boot 核心依赖

### 2.2 版本管理

在主 `pom.xml` 文件中配置了以下版本：

```xml
<dubbo.version>3.2.0</dubbo.version>
<dubbo-spring-boot.version>3.2.0</dubbo-spring-boot.version>
```

## 3. 配置项说明

### 3.1 应用配置

```yaml
dubbo:
  application:
    name: application-name       # 应用名称
    module: module-name           # 模块名称
    version: 1.0.0                # 版本
    organization: organization    # 组织
    environment: dev              # 环境
    owner: owner                  # 负责人
    parameters:                   # 应用参数
      key1: value1
      key2: value2
```

### 3.2 注册中心配置

```yaml
dubbo:
  registry:
    address: nacos://localhost:8848  # 注册中心地址
    username: nacos                  # 注册中心用户名
    password: nacos                  # 注册中心密码
    timeout: 30s                     # 注册中心超时时间
    cluster: cluster-name            # 注册中心集群
    group: dubbo                     # 注册中心分组
    namespace: public                # 注册中心命名空间
    parameters:                      # 注册中心参数
      key1: value1
      key2: value2
```

### 3.3 协议配置

```yaml
dubbo:
  protocol:
    name: dubbo                      # 协议名称
    port: 20880                      # 协议端口
    host: 0.0.0.0                    # 协议主机
    threadpool: fixed                # 线程池类型
    threads: 200                     # 线程池大小
    queues: 0                        # 线程池队列大小
    serialization: hessian2          # 序列化方式
    timeout: 10s                     # 超时时间
    connections: 10                  # 连接数
    weight: 100                      # 权重
    parameters:                      # 协议参数
      key1: value1
      key2: value2
```

### 3.4 服务提供者配置

```yaml
dubbo:
  provider:
    timeout: 10s                     # 超时时间
    retries: 0                       # 重试次数
    loadbalance: random              # 负载均衡策略
    cluster: failover                # 集群策略
    weight: 100                      # 权重
    group: wbw                       # 分组
    version: 1.0.0                   # 版本
    parameters:                      # 服务参数
      key1: value1
      key2: value2
```

### 3.5 服务消费者配置

```yaml
dubbo:
  consumer:
    timeout: 10s                     # 超时时间
    retries: 0                       # 重试次数
    loadbalance: random              # 负载均衡策略
    cluster: failover                # 集群策略
    group: wbw                       # 分组
    version: 1.0.0                   # 版本
    check: false                     # 是否检查服务存在
    async: false                     # 是否启用异步调用
    parameters:                      # 服务参数
      key1: value1
      key2: value2
```

## 4. 使用方法

### 4.1 定义服务接口

```java
public interface UserService {
    String getUserById(Long id);
    Boolean createUser(String userInfo);
    Boolean updateUser(Long id, String userInfo);
    Boolean deleteUser(Long id);
    String testService(String message);
}
```

### 4.2 实现服务提供者

```java
@DubboService(interfaceClass = UserService.class, version = "1.0.0", group = "wbw")
public class UserServiceImpl implements UserService {
    // 实现方法...
}
```

### 4.3 引用服务消费者

```java
@Component
public class UserServiceConsumer {
    
    @DubboReference(
            interfaceClass = UserService.class,
            version = "1.0.0",
            group = "wbw",
            check = false,
            timeout = 5000,
            retries = 1,
            cluster = "failover",
            mock = "com.wbw.dubbo.service.UserServiceFallback"
    )
    private UserService userService;
    
    // 调用方法...
}
```

### 4.4 实现服务降级

```java
public class UserServiceFallback implements UserService {
    // 实现降级方法...
}
```

## 5. 最佳实践

### 5.1 服务注册与发现

- 使用 Nacos 作为注册中心，提供高可用的服务注册与发现机制
- 为服务设置合理的分组和版本，便于服务管理和灰度发布
- 配置适当的超时时间，避免服务调用阻塞

### 5.2 通信协议

- 默认使用 Dubbo 协议，适用于高性能场景
- 选择合适的序列化方式，hessian2 是推荐的序列化方式
- 根据服务特性配置合理的线程池大小和类型

### 5.3 负载均衡

- 随机负载均衡：适用于大多数场景，简单高效
- 轮询负载均衡：适用于服务性能相近的场景
- 一致性哈希负载均衡：适用于有状态服务
- 最少活跃数负载均衡：适用于服务性能差异较大的场景

### 5.4 超时与重试

- 为每个服务设置合理的超时时间，避免服务调用阻塞
- 对于非幂等操作，设置重试次数为 0
- 对于幂等操作，可以设置适当的重试次数

### 5.5 服务降级与容错

- 实现服务降级策略，当服务不可用时返回默认值
- 使用 failover 集群策略，当服务调用失败时自动重试其他服务实例
- 配置合理的容错机制，提高系统的可用性

## 6. 验证步骤

### 6.1 功能测试

1. 运行 `UserServiceTest` 测试服务提供者的基本功能
2. 验证服务的增删改查操作是否正常
3. 验证服务的异常处理是否正确

### 6.2 压力测试

1. 运行 `UserServiceStressTest` 测试高并发场景下的服务性能
2. 观察并发线程数、请求数、响应时间和 QPS 等指标
3. 验证服务在高并发场景下的稳定性

### 6.3 容错测试

1. 模拟服务提供者不可用的场景
2. 验证服务降级策略是否生效
3. 验证服务消费者是否能够正常处理服务不可用的情况

## 7. 常见问题

### 7.1 服务注册失败

- 检查 Nacos 注册中心是否正常运行
- 检查网络连接是否正常
- 检查配置的注册中心地址是否正确

### 7.2 服务调用超时

- 检查服务提供者是否正常运行
- 检查网络连接是否正常
- 调整服务调用的超时时间

### 7.3 服务降级不生效

- 检查降级类是否正确实现
- 检查 `mock` 配置是否正确
- 检查服务消费者的配置是否正确

### 7.4 负载均衡不生效

- 检查服务是否有多个实例
- 检查负载均衡策略配置是否正确
- 检查服务提供者的权重配置是否正确

## 8. 总结

本 Dubbo 配置方案提供了完整的服务治理能力，包括服务注册与发现、负载均衡、服务降级与容错等核心功能。通过合理的配置和最佳实践，可以构建高性能、高可用的微服务系统。

## 9. 版本历史

- 1.0.0：初始版本，基于 Dubbo 3.2.0 实现
