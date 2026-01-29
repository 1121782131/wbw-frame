# 企业级微服务框架项目介绍

## 1. 项目概述

本项目是一个企业级微服务框架，基于 Spring Boot 3.2.0 和 Spring Cloud 2023.0.0 构建，提供了一套完整的微服务开发解决方案。框架集成了多种主流中间件和工具，旨在简化企业级应用的开发、部署和管理过程，提高开发效率和系统稳定性。

## 2. 核心功能

- **通用工具**：提供了丰富的常量定义、异常处理、结果封装和工具类
- **服务调用**：基于 Dubbo 实现的高性能服务调用框架
- **数据访问**：集成 MyBatis Plus 实现的 ORM 框架，支持动态数据源
- **服务发现与配置**：基于 Nacos 实现的服务注册、发现和配置中心
- **缓存**：集成 Redis 实现的缓存解决方案
- **消息队列**：基于 RocketMQ 实现的消息队列解决方案
- **安全认证**：基于 JWT 实现的安全认证框架
- **Web 功能**：集成 Spring Web 和 OpenAPI 文档

## 3. 技术架构

### 3.1 技术栈

- **基础框架**：Spring Boot 3.2.0, Spring Cloud 2023.0.0, Spring Cloud Alibaba 2023.0.1.0
- **服务调用**：Dubbo 3.2.0
- **服务发现与配置**：Nacos
- **数据访问**：MyBatis Plus 3.5.5
- **缓存**：Redis
- **消息队列**：RocketMQ
- **安全认证**：JWT
- **开发语言**：Java 17

### 3.2 模块结构

```
wbw-frame/
├── wbw-common/         # 通用工具模块
├── wbw-dubbo/          # Dubbo服务调用模块
├── wbw-mybatis/        # MyBatis数据访问模块
├── wbw-nacos/          # Nacos服务发现与配置模块
├── wbw-redis/          # Redis缓存模块
├── wbw-rocketmq/       # RocketMQ消息队列模块
├── wbw-security/       # 安全认证模块
├── wbw-starter/        # 启动器模块
├── wbw-web/            # Web模块
└── pom.xml             # 项目依赖管理
```

## 4. 适用场景

- **企业级微服务应用**：适用于构建大型、分布式的企业级应用
- **高并发场景**：通过优化的线程池配置和缓存机制，支持高并发访问
- **复杂业务系统**：提供了完整的业务支撑能力，适用于复杂业务逻辑的实现
- **快速开发**：集成了多种常用组件，简化开发流程，提高开发效率

## 5. 整体安装指南

### 5.1 环境要求

- JDK 17+
- Maven 3.6+
- Nacos Server 2.0+
- Redis Server 6.0+
- RocketMQ 4.9+

### 5.2 安装步骤

1. **克隆项目**
   ```bash
   git clone https://gitee.com/wang0306/frame.git
   cd frame
   ```

2. **编译打包**
   ```bash
   mvn clean install -DskipTests
   ```

3. **配置依赖**
   在您的项目中添加以下依赖：
   ```xml
   <dependency>
       <groupId>com.wbw</groupId>
       <artifactId>wbw-starter</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

4. **配置文件**
   根据您的环境配置 `application.yml` 文件，参考各模块的详细配置说明。

## 6. 各模块详细使用说明

### 6.1 wbw-common 通用工具模块

#### 6.1.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.1.2 核心功能

- **常量定义**：提供了业务常量、通用常量和Redis常量
- **异常处理**：定义了多种业务异常类型
- **结果封装**：提供了统一的响应结果封装
- **工具类**：提供了日期、JSON、数字、字符串等常用工具类

#### 6.1.3 使用示例

**1. 响应结果使用**
```java
import com.wbw.common.result.Result;

// 成功响应
return Result.success("操作成功");

// 带数据的成功响应
return Result.success("操作成功", data);

// 失败响应
return Result.fail("操作失败");

// 分页响应
return Result.success(PageResult.of(list, total, page, size));
```

**2. 异常使用**
```java
import com.wbw.common.exception.BusinessException;

// 抛出业务异常
if (condition) {
    throw new BusinessException("业务逻辑错误");
}
```

**3. 工具类使用**
```java
import com.wbw.common.utils.date.FrameDateUtil;
import com.wbw.common.utils.json.JsonUtil;

// 日期工具
String dateStr = FrameDateUtil.formatDate(new Date());

// JSON工具
String jsonStr = JsonUtil.toJson(data);
Object obj = JsonUtil.parseObject(jsonStr, Object.class);
```

### 6.2 wbw-dubbo Dubbo服务调用模块

#### 6.2.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-dubbo</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.2.2 配置文件

在 `application.yml` 文件中添加Dubbo配置：
```yaml
dubbo:
  application:
    name: your-application-name
  registry:
    address: nacos://localhost:8848
  protocol:
    name: dubbo
    port: 20880
  provider:
    timeout: 10s
    retries: 0
  consumer:
    timeout: 10s
    retries: 0
    check: false
```

#### 6.2.3 使用示例

**1. 定义服务接口**
```java
public interface UserService {
    String getUserById(Long id);
    Boolean createUser(String userInfo);
}
```

**2. 实现服务提供者**
```java
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = UserService.class, version = "1.0.0", group = "wbw")
public class UserServiceImpl implements UserService {
    @Override
    public String getUserById(Long id) {
        return "User{id=" + id + ", name=\"test_user\", age=25}";
    }

    @Override
    public Boolean createUser(String userInfo) {
        return true;
    }
}
```

**3. 引用服务消费者**
```java
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
public class UserServiceConsumer {
    @DubboReference(
            interfaceClass = UserService.class,
            version = "1.0.0",
            group = "wbw",
            check = false,
            timeout = 5000,
            retries = 1
    )
    private UserService userService;

    public String getUserInfo(Long id) {
        return userService.getUserById(id);
    }
}
```

### 6.3 wbw-mybatis MyBatis数据访问模块

#### 6.3.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.3.2 配置文件

在 `application.yml` 文件中添加数据源配置：
```yaml
spring:
  datasource:
    primary:
      url: jdbc:mysql://localhost:3306/primary_db
      username: root
      password: root
    secondary:
      url: jdbc:mysql://localhost:3306/secondary_db
      username: root
      password: root
```

#### 6.3.3 使用示例

**1. 动态数据源切换**
```java
import com.wbw.mybatis.annotation.DataSource;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // 使用主数据源
    public User getPrimaryUser(Long id) {
        return userMapper.selectById(id);
    }

    // 使用从数据源
    @DataSource("secondary")
    public User getSecondaryUser(Long id) {
        return userMapper.selectById(id);
    }
}
```

**2. 自定义Mapper**
```java
import com.wbw.mybatis.mapper.BaseMapperX;

public interface UserMapper extends BaseMapperX<User> {
    // 继承了BaseMapperX的所有方法
    // 可以添加自定义方法
}
```

### 6.4 wbw-nacos Nacos服务发现与配置模块

#### 6.4.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-nacos</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.4.2 配置文件

在 `application.yml` 文件中添加Nacos配置：
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: public
      config:
        server-addr: localhost:8848
        namespace: public
        file-extension: yaml
```

#### 6.4.3 使用示例

**1. 服务注册与发现**
```java
import com.wbw.nacos.service.NacosDiscoveryService;

@Service
public class DiscoveryService {
    @Autowired
    private NacosDiscoveryService nacosDiscoveryService;

    // 注册服务
    public void registerService() {
        nacosDiscoveryService.registerService();
    }

    // 发现服务
    public List<String> discoverServices() {
        return nacosDiscoveryService.discoverServices("service-name");
    }
}
```

**2. 配置中心**
```java
import com.wbw.nacos.service.NacosConfigService;

@Service
public class ConfigService {
    @Autowired
    private NacosConfigService nacosConfigService;

    // 获取配置
    public String getConfig(String dataId, String group) {
        return nacosConfigService.getConfig(dataId, group);
    }

    // 监听配置变更
    public void listenConfig(String dataId, String group) {
        nacosConfigService.listenConfig(dataId, group, config -> {
            System.out.println("配置变更: " + config);
        });
    }
}
```

### 6.5 wbw-redis Redis缓存模块

#### 6.5.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-redis</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.5.2 配置文件

在 `application.yml` 文件中添加Redis配置：
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

#### 6.5.3 使用示例

**1. Redis服务使用**
```java
import com.wbw.redis.service.RedisService;

@Service
public class CacheService {
    @Autowired
    private RedisService redisService;

    // 设置缓存
    public void setCache(String key, Object value, long timeout) {
        redisService.set(key, value, timeout);
    }

    // 获取缓存
    public <T> T getCache(String key, Class<T> clazz) {
        return redisService.get(key, clazz);
    }

    // 删除缓存
    public void deleteCache(String key) {
        redisService.delete(key);
    }
}
```

**2. Redis分布式锁**
```java
import com.wbw.redis.tool.RedisLock;

@Service
public class LockService {
    @Autowired
    private RedisLock redisLock;

    // 使用分布式锁
    public void doWithLock(String key, long expireTime, Runnable task) {
        if (redisLock.lock(key, expireTime)) {
            try {
                task.run();
            } finally {
                redisLock.unlock(key);
            }
        }
    }
}
```

### 6.6 wbw-rocketmq RocketMQ消息队列模块

#### 6.6.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-rocketmq</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.6.2 配置文件

在 `application.yml` 文件中添加RocketMQ配置：
```yaml
rocketmq:
  name-server: localhost:9876
  producer:
    group: producer-group
  consumer:
    group: consumer-group
```

#### 6.6.3 使用示例

**1. 消息发送**
```java
import com.wbw.rocketmq.producer.RocketMQProducerService;

@Service
public class MessageService {
    @Autowired
    private RocketMQProducerService rocketMQProducerService;

    // 发送同步消息
    public void sendSyncMessage(String topic, String tag, String message) {
        rocketMQProducerService.sendSyncMessage(topic, tag, message);
    }

    // 发送异步消息
    public void sendAsyncMessage(String topic, String tag, String message) {
        rocketMQProducerService.sendAsyncMessage(topic, tag, message, (sendResult, e) -> {
            if (e != null) {
                System.err.println("发送失败: " + e.getMessage());
            } else {
                System.out.println("发送成功: " + sendResult);
            }
        });
    }

    // 发送延迟消息
    public void sendDelayMessage(String topic, String tag, String message, int delayLevel) {
        rocketMQProducerService.sendDelayMessage(topic, tag, message, delayLevel);
    }
}
```

**2. 消息消费**
```java
import com.wbw.rocketmq.consumer.RocketMQConsumerService;

@Service
public class ConsumerService {
    @Autowired
    private RocketMQConsumerService rocketMQConsumerService;

    // 消费消息
    public void consumeMessage(String topic, String tag) {
        rocketMQConsumerService.subscribe(topic, tag, message -> {
            System.out.println("收到消息: " + message);
            return true; // 返回true表示消费成功
        });
    }
}
```

### 6.7 wbw-security 安全认证模块

#### 6.7.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.7.2 配置文件

在 `application.yml` 文件中添加JWT配置：
```yaml
wbw:
  security:
    jwt:
      secret: your-secret-key
      expire: 3600
      refresh-expire: 7200
```

#### 6.7.3 使用示例

**1. 生成Token**
```java
import com.wbw.security.service.JwtTokenService;
import com.wbw.security.model.TokenRequest;

@Service
public class AuthService {
    @Autowired
    private JwtTokenService jwtTokenService;

    // 生成Token
    public String generateToken(String username, String password) {
        TokenRequest request = new TokenRequest();
        request.setUsername(username);
        request.setPassword(password);
        return jwtTokenService.generateToken(request);
    }

    // 验证Token
    public boolean validateToken(String token) {
        return jwtTokenService.validateToken(token);
    }

    // 刷新Token
    public String refreshToken(String token) {
        return jwtTokenService.refreshToken(token);
    }
}
```

**2. 注解使用**
```java
import com.wbw.security.annotation.JwtToken;

@RestController
@RequestMapping("/api")
public class UserController {
    // 需要认证的接口
    @JwtToken
    @GetMapping("/user")
    public User getUser() {
        // 从上下文获取用户信息
        return JwtSecurityContext.getCurrentUser();
    }

    // 不需要认证的接口
    @Anonymous
    @PostMapping("/login")
    public String login(String username, String password) {
        // 登录逻辑
        return authService.generateToken(username, password);
    }
}
```

### 6.8 wbw-web Web模块

#### 6.8.1 安装步骤

在项目的 `pom.xml` 文件中添加依赖：
```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-web</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 6.8.2 配置文件

在 `application.yml` 文件中添加Web配置：
```yaml
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

# OpenAPI配置
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

#### 6.8.3 使用示例

**1. 全局异常处理**

框架已集成全局异常处理器，会自动捕获并处理以下异常：
- BusinessException
- ServiceException
- ValidationException
- SystemException
- TokenException
- UserAuthenticationException

**2. 日志拦截**

框架已集成请求日志拦截器，会自动记录请求和响应信息。

**3. 验证器使用**
```java
import com.wbw.web.validator.Phone;

public class UserDTO {
    @Phone(message = "手机号格式错误")
    private String phone;

    // getter and setter
}

@RestController
@RequestMapping("/api")
public class UserController {
    @PostMapping("/user")
    public Result createUser(@Valid @RequestBody UserDTO userDTO) {
        // 处理逻辑
        return Result.success("创建成功");
    }
}
```

## 7. 总结

本项目是一个功能全面的企业级微服务框架，集成了多种主流中间件和工具，旨在简化企业级应用的开发、部署和管理过程。框架提供了清晰的模块划分和详细的使用说明，方便开发人员快速上手和使用。

通过本框架，开发人员可以专注于业务逻辑的实现，而无需关注底层技术细节，从而提高开发效率和系统稳定性。框架适用于构建大型、分布式的企业级应用，支持高并发场景和复杂业务系统的实现。

## 8. 版本历史

- **1.0.0**：初始版本，包含所有核心模块的实现

---

以上是项目的全面介绍和使用说明，希望对开发人员有所帮助。如有任何问题，请参考各模块的详细文档或联系项目维护人员。