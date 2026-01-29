# wbw-frame 集成指南

## 1. 项目概述

wbw-frame 是一个企业级微服务框架，提供了完整的微服务开发基础设施，包括：

- **wbw-common**：通用工具模块
- **wbw-security**：安全认证模块
- **wbw-web**：Web通用模块
- **wbw-mybatis**：MyBatis增强模块
- **wbw-redis**：Redis配置和工具模块
- **wbw-nacos**：Nacos服务发现与配置中心集成
- **wbw-rocketmq**：RocketMQ客户端包
- **wbw-dubbo**：Dubbo服务调用模块

## 2. 依赖关系分析

### 2.1 模块间依赖关系

| 模块 | 依赖模块 | 功能描述 |
| --- | --- | --- |
| wbw-common | 无 | 通用工具类，被其他所有模块依赖 |
| wbw-security | wbw-common | 安全认证与授权 |
| wbw-web | wbw-common | Web通用功能，如异常处理、拦截器等 |
| wbw-mybatis | 无 | MyBatis增强，提供动态数据源和事务管理 |
| wbw-redis | wbw-common | Redis配置和工具 |
| wbw-nacos | wbw-common | Nacos服务发现与配置中心集成 |
| wbw-rocketmq | wbw-common | RocketMQ客户端集成 |
| wbw-dubbo | wbw-common | Dubbo服务调用 |

### 2.2 需要依赖此框架的项目组件类型

1. **Web应用**：需要集成 wbw-web、wbw-security 模块
2. **微服务**：需要集成 wbw-dubbo、wbw-nacos 模块
3. **数据访问层**：需要集成 wbw-mybatis 模块
4. **缓存应用**：需要集成 wbw-redis 模块
5. **消息驱动应用**：需要集成 wbw-rocketmq 模块
6. **通用业务模块**：需要集成 wbw-common 模块

## 3. 集成指南

### 3.1 环境配置要求

- **JDK**：17+
- **Maven**：3.6+
- **Spring Boot**：3.2.0+
- **Spring Cloud**：2023.0.0+
- **Spring Cloud Alibaba**：2023.0.1.0+

### 3.2 安装步骤

#### 3.2.1 克隆代码库

```bash
git clone <repository-url>
cd wbw-frame
```

#### 3.2.2 构建项目

```bash
mvn clean install -DskipTests
```

#### 3.2.3 在目标项目中添加依赖

在目标项目的 pom.xml 文件中添加以下依赖管理配置：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.wbw</groupId>
            <artifactId>wbw-frame</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

然后根据需要添加具体模块依赖，例如：

```xml
<dependencies>
    <!-- 通用模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-common</artifactId>
    </dependency>
    
    <!-- 安全模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-security</artifactId>
    </dependency>
    
    <!-- Web模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-web</artifactId>
    </dependency>
    
    <!-- MyBatis模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-mybatis</artifactId>
    </dependency>
    
    <!-- Redis模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-redis</artifactId>
    </dependency>
    
    <!-- Nacos模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-nacos</artifactId>
    </dependency>
    
    <!-- RocketMQ模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-rocketmq</artifactId>
    </dependency>
    
    <!-- Dubbo模块 -->
    <dependency>
        <groupId>com.wbw</groupId>
        <artifactId>wbw-dubbo</artifactId>
    </dependency>
</dependencies>
```

### 3.3 初始化方法

#### 3.3.1 基础配置

在 application.yml 文件中添加以下基础配置：

```yaml
# 应用配置
spring:
  application:
    name: your-application-name

# 环境配置
server:
  port: 8080
```

#### 3.3.2 各模块初始化配置

##### wbw-common 模块

无需特殊配置，直接引入即可使用。

##### wbw-security 模块

```yaml
# 安全配置
wbw:
  security:
    jwt:
      secret: your-secret-key
      expire: 3600  # 过期时间（秒）
      issuer: your-issuer
```

##### wbw-web 模块

无需特殊配置，直接引入即可使用。

##### wbw-mybatis 模块

```yaml
# MyBatis配置
wbw:
  mybatis:
    # 启用动态数据源
    dynamic-data-source-enabled: true
    # 动态数据源配置
    dynamic-datasource:
      # 默认数据源
      primary: master
      # 数据源配置
      datasources:
        # 主数据源
        master:
          url: jdbc:mysql://localhost:3306/master_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
          username: root
          password: 123456
          driver-class-name: com.mysql.cj.jdbc.Driver
        # 从数据源
        slave:
          url: jdbc:mysql://localhost:3306/slave_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
          username: root
          password: 123456
          driver-class-name: com.mysql.cj.jdbc.Driver
```

##### wbw-redis 模块

```yaml
# Redis配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
    timeout: 3000ms
    jedis:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

##### wbw-nacos 模块

```yaml
# Nacos配置
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

##### wbw-rocketmq 模块

```yaml
# RocketMQ配置
rocketmq:
  name-server: localhost:9876
  producer:
    group: default-producer-group
    send-message-timeout: 3000
    retry-times-when-send-failed: 2
  consumer:
    group: default-consumer-group
    consume-thread-min: 20
    consume-thread-max: 64
    message-model: CLUSTERING
```

##### wbw-dubbo 模块

```yaml
# Dubbo配置
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

### 3.4 核心API调用示例

#### 3.4.1 wbw-common 模块

```java
// 使用通用常量
import com.wbw.common.constant.CommonConstant;

// 使用通用结果类
import com.wbw.common.result.Result;

// 使用工具类
import com.wbw.common.utils.string.StringUtil;
import com.wbw.common.utils.date.FrameDateUtil;
import com.wbw.common.utils.json.JsonUtil;

// 示例代码
public class CommonExample {
    public Result<String> process(String input) {
        if (StringUtil.isEmpty(input)) {
            return Result.failed("输入不能为空");
        }
        
        String processed = StringUtil.trim(input);
        String json = JsonUtil.toJson(processed);
        String currentDate = FrameDateUtil.getCurrentDateString();
        
        return Result.success("处理成功: " + json + " (" + currentDate + ")");
    }
}
```

#### 3.4.2 wbw-security 模块

```java
// 导入相关类
import com.wbw.security.annotation.JwtToken;
import com.wbw.security.service.JwtTokenService;
import com.wbw.security.model.TokenInfo;
import com.wbw.security.model.JwtUser;

// 示例代码
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @PostMapping("/login")
    public Result<TokenInfo> login(@RequestBody LoginRequest request) {
        // 验证用户
        // ...
        
        // 创建JwtUser对象
        JwtUser user = new JwtUser();
        user.setId(1L);
        user.setUsername(request.getUsername());
        user.setRoles(Collections.singletonList("USER"));
        
        // 生成token
        TokenInfo tokenInfo = jwtTokenService.generateToken(user);
        return Result.success(tokenInfo);
    }
    
    @GetMapping("/userinfo")
    @JwtToken
    public Result<JwtUser> getUserInfo() {
        // 从上下文获取当前用户
        JwtUser currentUser = JwtSecurityContext.getCurrentUser();
        return Result.success(currentUser);
    }
}
```

#### 3.4.3 wbw-web 模块

```java
// 导入相关类
import com.wbw.web.util.WebUtils;

// 示例代码
@RestController
@RequestMapping("/api")
public class ApiController {
    
    @GetMapping("/test")
    public Result<String> test() {
        // 获取客户端IP
        String clientIp = WebUtils.getClientIp();
        return Result.success("Hello World! Your IP is: " + clientIp);
    }
}
```

#### 3.4.4 wbw-mybatis 模块

```java
// 导入相关类
import com.wbw.mybatis.annotation.DataSource;
import com.wbw.mybatis.mapper.BaseMapperX;

// 示例代码
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    // 使用默认数据源（master）
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
    
    // 使用指定数据源（slave）
    @DataSource("slave")
    public List<User> listUsers() {
        return userMapper.selectList(null);
    }
    
    // 批量插入
    public boolean batchInsert(List<User> users) {
        return userMapper.insertBatch(users);
    }
    
    // 批量更新
    public boolean batchUpdate(List<User> users) {
        return userMapper.updateBatch(users);
    }
}

// 自定义Mapper
public interface UserMapper extends BaseMapperX<User> {
}
```

#### 3.4.5 wbw-redis 模块

```java
// 导入相关类
import com.wbw.redis.service.RedisService;
import com.wbw.redis.tool.RedisLock;

// 示例代码
@Service
public class RedisExampleService {
    
    @Autowired
    private RedisService redisService;
    
    public void setValue(String key, String value) {
        redisService.set(key, value);
    }
    
    public String getValue(String key) {
        return redisService.get(key);
    }
    
    public void testLock() {
        // 使用Redis分布式锁
        try (RedisLock lock = new RedisLock(redisService, "test-lock", 10)) {
            if (lock.acquire()) {
                // 执行业务逻辑
                System.out.println("获取锁成功，执行业务逻辑");
                Thread.sleep(5000);
            } else {
                System.out.println("获取锁失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 3.4.6 wbw-nacos 模块

```java
// 导入相关类
import com.wbw.nacos.service.NacosConfigService;
import com.wbw.nacos.service.NacosDiscoveryService;

// 示例代码
@Service
public class NacosExampleService {
    
    @Autowired
    private NacosConfigService nacosConfigService;
    
    @Autowired
    private NacosDiscoveryService nacosDiscoveryService;
    
    public void testConfig() {
        // 获取配置
        String config = nacosConfigService.getConfig("application", "DEFAULT_GROUP", 5000);
        System.out.println("获取到的配置: " + config);
        
        // 监听配置变化
        nacosConfigService.addConfigListener("application", "DEFAULT_GROUP", configInfo -> {
            System.out.println("配置发生变化: " + configInfo);
        });
    }
    
    public void testDiscovery() {
        // 获取服务列表
        List<String> services = nacosDiscoveryService.getServices();
        System.out.println("服务列表: " + services);
        
        // 获取服务实例
        List<Instance> instances = nacosDiscoveryService.getInstances("your-service-name");
        System.out.println("服务实例: " + instances);
    }
}
```

#### 3.4.7 wbw-rocketmq 模块

```java
// 导入相关类
import com.wbw.rocketmq.producer.RocketMQProducerService;
import com.wbw.rocketmq.consumer.RocketMQConsumerService;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

// 示例代码
@Service
public class RocketMQExampleService {
    
    @Autowired
    private RocketMQProducerService rocketMQProducerService;
    
    @Autowired
    private RocketMQConsumerService rocketMQConsumerService;
    
    @PostConstruct
    public void init() throws Exception {
        // 注册消费者
        rocketMQConsumerService.registerClusterConsumer(
            "test-topic",
            "*",
            "test-consumer-group",
            new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt msg : msgs) {
                        System.out.println("收到消息: " + new String(msg.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        );
    }
    
    public void sendMessage(String content) {
        // 发送同步消息
        rocketMQProducerService.send("test-topic", content);
        
        // 发送延迟消息
        rocketMQProducerService.sendDelay("test-topic", "延迟消息: " + content, 3);
        
        // 发送事务消息
        rocketMQProducerService.sendTransaction("test-topic", "事务消息: " + content, "transaction-arg");
    }
}
```

#### 3.4.8 wbw-dubbo 模块

```java
// 导入相关类
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.DubboReference;

// 服务接口
public interface UserService {
    String getUserById(Long id);
    Boolean createUser(String userInfo);
}

// 服务提供者
@DubboService(interfaceClass = UserService.class, version = "1.0.0", group = "wbw")
public class UserServiceImpl implements UserService {
    @Override
    public String getUserById(Long id) {
        // 实现逻辑
        return "User: " + id;
    }
    
    @Override
    public Boolean createUser(String userInfo) {
        // 实现逻辑
        return true;
    }
}

// 服务消费者
@Service
public class UserServiceConsumer {
    
    @DubboReference(
        interfaceClass = UserService.class,
        version = "1.0.0",
        group = "wbw",
        check = false,
        timeout = 5000,
        retries = 1,
        cluster = "failover"
    )
    private UserService userService;
    
    public String getUserInfo(Long id) {
        return userService.getUserById(id);
    }
    
    public Boolean createNewUser(String userInfo) {
        return userService.createUser(userInfo);
    }
}
```

### 3.5 常见问题解决方案

#### 3.5.1 依赖冲突问题

**症状**：项目启动时出现类冲突或版本不兼容错误。

**解决方案**：
- 检查依赖树，使用 `mvn dependency:tree` 命令查看依赖关系
- 在 pom.xml 中使用 `<dependencyManagement>` 统一管理版本
- 使用 `<exclusions>` 排除冲突的依赖

#### 3.5.2 配置文件加载问题

**症状**：配置项未生效或加载失败。

**解决方案**：
- 检查配置文件路径和命名是否正确
- 确保配置项名称与框架要求一致
- 检查配置文件格式是否正确（YAML/Properties）

#### 3.5.3 服务注册与发现问题

**症状**：Dubbo服务无法注册到Nacos或无法发现其他服务。

**解决方案**：
- 检查Nacos服务是否正常运行
- 检查网络连接是否畅通
- 确保服务名称、分组和版本配置正确
- 检查Dubbo协议配置是否正确

#### 3.5.4 数据库连接问题

**症状**：无法连接数据库或数据源切换失败。

**解决方案**：
- 检查数据库连接信息是否正确
- 确保数据库服务正常运行
- 检查数据源配置是否符合框架要求
- 确保在事务开始前切换数据源

#### 3.5.5 消息发送与消费问题

**症状**：RocketMQ消息发送失败或消费异常。

**解决方案**：
- 检查RocketMQ服务是否正常运行
- 确保NameServer地址配置正确
- 检查消息主题和标签是否存在
- 确保消费者组配置正确
- 实现幂等消费，处理重复消息

## 4. 框架集成到启动流程

### 4.1 是否需要集成到启动流程

是的，wbw-frame 的大部分模块需要集成到项目的启动流程中，以确保在应用启动时正确初始化和配置。

### 4.2 集成方案

#### 4.2.1 使用 @SpringBootApplication 注解

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### 4.2.2 自动配置

wbw-frame 的模块都使用了 Spring Boot 的自动配置机制，通过在 `META-INF/spring.factories` 文件中声明自动配置类，实现了自动集成到 Spring Boot 应用中。

#### 4.2.3 启动参数配置

可以通过以下方式配置启动参数：

1. **命令行参数**：
   ```bash
   java -jar your-application.jar --server.port=8080 --spring.profiles.active=prod
   ```

2. **环境变量**：
   ```bash
   export SERVER_PORT=8080
   export SPRING_PROFILES_ACTIVE=prod
   java -jar your-application.jar
   ```

3. **配置文件**：
   ```yaml
   # application.yml
   server:
     port: 8080
   
   spring:
     profiles:
       active: prod
   ```

### 4.3 启动流程说明

1. **应用启动**：执行 `SpringApplication.run()` 方法
2. **自动配置**：Spring Boot 加载 `META-INF/spring.factories` 中的自动配置类
3. **模块初始化**：各模块的自动配置类执行初始化逻辑
   - 加载配置项
   - 初始化组件
   - 注册服务
4. **应用就绪**：所有模块初始化完成，应用开始接收请求

## 5. 最佳实践

### 5.1 模块选择

根据项目需求选择合适的模块，避免引入不必要的依赖。

### 5.2 配置管理

- 使用 Nacos 作为配置中心，实现配置的集中管理和动态更新
- 按环境分离配置文件（dev、test、prod）
- 敏感配置使用加密存储

### 5.3 依赖管理

- 使用 `<dependencyManagement>` 统一管理依赖版本
- 定期更新依赖版本，修复安全漏洞
- 避免依赖冲突

### 5.4 性能优化

- 合理配置线程池大小
- 优化数据库连接池配置
- 使用缓存减少数据库访问
- 合理使用异步处理

### 5.5 监控与告警

- 集成 Spring Boot Actuator 实现健康检查
- 使用 Prometheus 和 Grafana 实现监控
- 配置合理的告警规则

## 6. 总结

wbw-frame 提供了完整的微服务开发基础设施，通过本文档的指导，您可以快速将框架集成到您的项目中。框架的模块化设计使得您可以根据需要选择合适的模块，灵活构建您的应用。

如果您在使用过程中遇到问题，请参考本文档的常见问题解决方案，或联系框架维护人员获取帮助。