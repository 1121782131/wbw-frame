# Nacos 服务发现与配置中心集成模块

## 模块介绍

`wbw-nacos` 是 wbw-frame 框架中的 Nacos 集成模块，提供了完整的 Nacos 服务发现与配置中心功能，包括：

- **服务注册与发现**：自动将服务注册到 Nacos，并支持通过服务名进行服务发现
- **配置中心**：支持从 Nacos 获取配置，支持配置动态刷新
- **健康检查**：集成 Spring Boot Actuator，提供 Nacos 健康检查端点
- **容错处理**：集成 Resilience4j，提供服务降级、熔断和限流功能

## 快速开始

### 1. 环境准备

- 安装并启动 Nacos Server
  - 下载地址：https://github.com/alibaba/nacos/releases
  - 启动命令：`sh startup.sh -m standalone`（Linux/Mac）或 `cmd startup.cmd -m standalone`（Windows）
  - 访问控制台：http://localhost:8848/nacos

### 2. 依赖配置

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-nacos</artifactId>
    <version>${wbw-frame.version}</version>
</dependency>
```

### 3. 配置文件

在 `application.yml` 文件中添加 Nacos 相关配置：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # Nacos服务地址
        service: ${spring.application.name}  # 服务名
        namespace:  # 命名空间（可选）
        group: DEFAULT_GROUP  # 分组（可选）
      config:
        server-addr: localhost:8848  # Nacos服务地址
        namespace:  # 命名空间（可选）
        group: DEFAULT_GROUP  # 分组（可选）
        file-extension: yml  # 配置文件类型

# Nacos健康检查配置
nacos:
  health:
    enabled: true  # 启用健康检查
    check-discovery: true  # 检查服务发现
    check-config: true  # 检查配置中心

# Nacos容错处理配置
nacos:
  fault-tolerance:
    enabled: true  # 启用容错处理
    circuit-breaker:
      enabled: true  # 启用服务熔断
    rate-limit:
      enabled: true  # 启用服务限流
      limit: 100  # 限流阈值（QPS）
```

### 4. 启用 Nacos

在 Spring Boot 应用的启动类上添加 `@EnableDiscoveryClient` 注解：

```java
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDiscoveryClient
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 使用示例

### 服务发现示例

#### 1. 自动服务注册

服务启动时会自动注册到 Nacos，无需手动操作。

#### 2. 服务发现

使用 `NacosDiscoveryService` 进行服务发现：

```java
import com.wbw.nacos.service.NacosDiscoveryService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiscoveryController {
    
    @Autowired
    private NacosDiscoveryService discoveryService;
    
    @GetMapping("/services")
    public List<String> getServices() throws Exception {
        return discoveryService.getServices();
    }
    
    @GetMapping("/instances/{serviceName}")
    public List<Instance> getInstances(String serviceName) throws Exception {
        return discoveryService.getHealthyInstances(serviceName);
    }
}
```

#### 3. 使用 RestTemplate 进行服务调用

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/call")
    public String callService() {
        // 使用服务名进行调用，会自动进行负载均衡
        return restTemplate.getForObject("http://service-provider/api/hello", String.class);
    }
}
```

### 配置中心示例

#### 1. 在 Nacos 控制台创建配置

- 登录 Nacos 控制台：http://localhost:8848/nacos
- 进入「配置管理」→「配置列表」
- 点击「+」按钮，创建配置：
  - Data ID: `service-consumer.yml`
  - Group: `DEFAULT_GROUP`
  - 配置格式: YAML
  - 配置内容:
    ```yaml
    app:
      name: service-consumer
      version: 1.0.0
      description: Service Consumer Application
    ```

#### 2. 读取配置

使用 `@Value` 注解读取配置：

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope  // 支持配置动态刷新
public class ConfigController {
    
    @Value("${app.name}")
    private String appName;
    
    @Value("${app.version}")
    private String appVersion;
    
    @Value("${app.description}")
    private String appDescription;
    
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("appName", appName);
        config.put("appVersion", appVersion);
        config.put("appDescription", appDescription);
        return config;
    }
}
```

#### 3. 使用 NacosConfigService 操作配置

```java
import com.wbw.nacos.service.NacosConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nacos/config")
public class NacosConfigController {
    
    @Autowired
    private NacosConfigService configService;
    
    @GetMapping("/{dataId}")
    public String getConfig(@PathVariable String dataId) throws Exception {
        return configService.getConfig(dataId);
    }
    
    @PostMapping("/{dataId}")
    public boolean publishConfig(@PathVariable String dataId, @RequestBody String content) throws Exception {
        return configService.publishConfig(dataId, content);
    }
    
    @DeleteMapping("/{dataId}")
    public boolean removeConfig(@PathVariable String dataId) throws Exception {
        return configService.removeConfig(dataId);
    }
}
```

### 容错处理示例

使用 `NacosFaultToleranceService` 进行容错处理：

```java
import com.wbw.nacos.service.NacosFaultToleranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaultToleranceController {
    
    @Autowired
    private NacosFaultToleranceService faultToleranceService;
    
    @GetMapping("/fault-tolerance")
    public String testFaultTolerance() {
        return faultToleranceService.executeWithFaultTolerance(
                "test-service",
                () -> {
                    // 正常业务逻辑
                    // 模拟异常
                    if (Math.random() > 0.5) {
                        throw new RuntimeException("Service error");
                    }
                    return "Service executed successfully";
                },
                () -> "Service degraded, using fallback"
        );
    }
}
```

## 配置说明

### 核心配置项

| 配置项 | 描述 | 默认值 |
| --- | --- | --- |
| `spring.cloud.nacos.discovery.server-addr` | Nacos服务地址 | localhost:8848 |
| `spring.cloud.nacos.discovery.service` | 服务名 | ${spring.application.name} |
| `spring.cloud.nacos.discovery.namespace` | 命名空间 | 空 |
| `spring.cloud.nacos.discovery.group` | 分组 | DEFAULT_GROUP |
| `spring.cloud.nacos.config.server-addr` | Nacos配置中心地址 | localhost:8848 |
| `spring.cloud.nacos.config.namespace` | 配置中心命名空间 | 空 |
| `spring.cloud.nacos.config.group` | 配置中心分组 | DEFAULT_GROUP |
| `spring.cloud.nacos.config.file-extension` | 配置文件类型 | yml |

### 健康检查配置

| 配置项 | 描述 | 默认值 |
| --- | --- | --- |
| `nacos.health.enabled` | 是否启用健康检查 | true |
| `nacos.health.check-discovery` | 是否检查服务发现 | true |
| `nacos.health.check-config` | 是否检查配置中心 | true |
| `nacos.health.timeout` | 健康检查超时时间（毫秒） | 3000 |

### 容错处理配置

| 配置项 | 描述 | 默认值 |
| --- | --- | --- |
| `nacos.fault-tolerance.enabled` | 是否启用容错处理 | true |
| `nacos.fault-tolerance.degrade.enabled` | 是否启用服务降级 | true |
| `nacos.fault-tolerance.circuit-breaker.enabled` | 是否启用服务熔断 | true |
| `nacos.fault-tolerance.rate-limit.enabled` | 是否启用服务限流 | true |
| `nacos.fault-tolerance.rate-limit.limit` | 限流阈值（QPS） | 100 |

## 健康检查

模块集成了 Spring Boot Actuator，提供了 Nacos 健康检查端点：

- 访问地址：`http://localhost:8080/actuator/health`
- 健康检查结果包含 Nacos 服务发现和配置中心的健康状态

## 注意事项

1. **版本兼容性**：
   - 本模块基于 Spring Boot 3.2.0 + Spring Cloud 2023.0.0 + Spring Cloud Alibaba 2023.0.1.0 开发
   - 使用时请确保版本匹配

2. **Nacos 服务配置**：
   - 生产环境建议部署 Nacos 集群，提高可用性
   - 配置中心建议使用命名空间和分组进行配置隔离

3. **容错处理**：
   - 容错处理功能基于 Resilience4j 实现
   - 请根据实际业务场景调整容错策略参数

4. **性能优化**：
   - 服务发现缓存：Nacos 客户端会缓存服务列表，减少网络请求
   - 配置中心缓存：配置中心会缓存配置内容，支持本地快照

5. **安全配置**：
   - 生产环境建议启用 Nacos 认证
   - 敏感配置建议使用加密存储

## 常见问题

### 1. 服务无法注册到 Nacos

- 检查 Nacos 服务是否正常运行
- 检查网络连接是否正常
- 检查配置文件中的 `server-addr` 是否正确
- 检查服务名是否配置

### 2. 无法获取 Nacos 配置

- 检查 Nacos 控制台是否已创建对应配置
- 检查配置的 Data ID、Group 是否正确
- 检查配置文件类型是否匹配
- 检查网络连接是否正常

### 3. 健康检查失败

- 检查 Nacos 服务是否正常运行
- 检查网络连接是否正常
- 检查健康检查配置是否正确

### 4. 容错处理不生效

- 检查容错处理配置是否启用
- 检查 Resilience4j 依赖是否正确
- 检查容错策略参数是否合理

## 相关文档

- [Nacos 官方文档](https://nacos.io/zh-cn/docs/quick-start.html)
- [Spring Cloud Alibaba 文档](https://spring-cloud-alibaba-group.github.io/github-pages/greenwich/spring-cloud-alibaba.html)
- [Resilience4j 文档](https://resilience4j.readme.io/docs)
