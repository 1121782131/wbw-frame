# 企业级微服务框架项目介绍

## 1. 项目概述

### 1.1 项目简介
企业级微服务框架（wbw-frame）是一个基于 Spring Boot 3.2.5 和 Spring Cloud 2023.0.1 的综合性微服务开发框架，旨在为企业级应用提供完整的微服务基础设施和开发工具链。

### 1.2 核心功能

| 功能模块 | 主要功能 | 技术实现 |
|---------|---------|----------|
| 公共模块 (wbw-common) | 统一返回结构、异常体系、工具类库 | Java 核心库 + 第三方工具 |
| 安全模块 (wbw-security) | JWT 认证、Token 管理、安全上下文 | Spring Security + JJWT |
| Web 模块 (wbw-web) | API 文档、全局异常处理、参数验证 | Spring MVC + SpringDoc |
| 数据模块 (wbw-mybatis) | 动态数据源、MyBatis Plus 集成 | MyBatis Plus + AOP |
| 缓存模块 (wbw-redis) | Redis 客户端封装、分布式锁 | Jedis + Lettuce |
| 服务治理 (wbw-nacos) | 服务发现、配置管理、健康检查 | Nacos 客户端 |
| 消息模块 (wbw-rocketmq) | 消息生产者、消费者封装 | RocketMQ Spring Boot Starter |
| RPC 模块 (wbw-dubbo) | 服务调用、过滤器、事务 | Dubbo 3.3.0 |
| 框架入口 (wbw-starter) | 自动配置、依赖管理 | Spring Boot 自动配置 |

### 1.3 技术栈
- **基础框架**：Spring Boot 3.2.5、Spring Cloud 2023.0.1、Spring Cloud Alibaba 2023.0.1.0
- **数据访问**：MyBatis Plus 3.5.6、ShardingSphere 5.4.2
- **消息队列**：RocketMQ 2.3.4
- **RPC 框架**：Dubbo 3.3.0
- **API 文档**：SpringDoc 2.3.0
- **安全认证**：JJWT 0.12.5
- **缓存**：Redis 5.1.0
- **服务治理**：Nacos 2023.0.1.0
- **工具库**：Lombok 1.18.32、Hutool 5.8.25、Guava 33.0.0-jre

## 2. 使用指南

### 2.1 环境要求
- **JDK**：Java 17 或更高版本
- **Maven**：Maven 3.8.0 或更高版本
- **IDE**：IntelliJ IDEA 2023.0 或更高版本（推荐）
- **操作系统**：Windows、Linux、macOS

### 2.2 安装步骤

#### 2.2.1 克隆项目
```bash
git clone <项目地址>
cd wbw-frame
```

#### 2.2.2 构建项目
```bash
mvn clean install -DskipTests
```

#### 2.2.3 在微服务项目中引入
在微服务项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2.3 基本操作流程

#### 2.3.1 创建微服务项目
1. 使用 Spring Initializr 创建一个新的 Spring Boot 项目
2. 在 `pom.xml` 中添加框架依赖
3. 配置必要的环境变量和配置文件

#### 2.3.2 启动服务
```bash
mvn spring-boot:run
```

#### 2.3.3 访问 API 文档
启动服务后，可通过以下地址访问 API 文档：
```
http://localhost:8080/swagger-ui.html
```

## 3. 配置说明

### 3.1 核心配置项

#### 3.1.1 框架基础配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.enabled | true | 是否启用框架 | 全局开关，可用于临时禁用框架功能 |
| wbw.framework.application-name | ${spring.application.name} | 应用名称 | 用于日志、监控等标识 |
| wbw.framework.environment | ${spring.profiles.active} | 环境标识 | 用于区分不同环境的配置 |

#### 3.1.2 安全模块配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.security.enabled | true | 是否启用安全模块 | 控制 JWT 认证功能 |
| wbw.framework.security.jwt-secret | 随机生成 | JWT 密钥 | 用于 Token 签名和验证 |
| wbw.framework.security.token-expiration | 3600000 | Token 过期时间（毫秒） | 控制 Token 有效期 |
| wbw.framework.security.token-issuer | wbw-framework | Token 签发者 | 用于 Token 标识 |

#### 3.1.3 Web 模块配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.web.open-api-enabled | true | 是否启用 OpenAPI | 控制 API 文档生成 |
| wbw.framework.web.api-prefix | /api | API 路径前缀 | 统一 API 路径格式 |
| wbw.framework.web.cors-enabled | true | 是否启用 CORS | 跨域请求支持 |
| wbw.framework.web.cors-allowed-origins | * | 允许的跨域来源 | 跨域请求配置 |

#### 3.1.4 数据模块配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.mybatis.enabled | true | 是否启用 MyBatis | 控制数据访问功能 |
| wbw.framework.mybatis.mapper-locations | classpath:mapper/**/*.xml | Mapper XML 位置 | 自定义 Mapper 配置 |
| wbw.framework.mybatis.type-aliases-package | com.wbw.**.entity | 实体类包路径 | 简化类型别名配置 |

#### 3.1.5 缓存模块配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.redis.enabled | true | 是否启用 Redis | 控制缓存功能 |
| wbw.framework.redis.host | localhost | Redis 主机地址 | Redis 连接配置 |
| wbw.framework.redis.port | 6379 | Redis 端口 | Redis 连接配置 |
| wbw.framework.redis.password | "" | Redis 密码 | Redis 安全配置 |
| wbw.framework.redis.database | 0 | Redis 数据库索引 | 多数据库隔离 |

#### 3.1.6 服务治理配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.nacos.enabled | true | 是否启用 Nacos | 控制服务治理功能 |
| wbw.framework.nacos.server-addr | localhost:8848 | Nacos 服务地址 | 服务注册与发现 |
| wbw.framework.nacos.namespace | public | Nacos 命名空间 | 多环境隔离 |
| wbw.framework.nacos.group | DEFAULT_GROUP | Nacos 分组 | 服务分组管理 |

#### 3.1.7 消息模块配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.rocketmq.enabled | true | 是否启用 RocketMQ | 控制消息队列功能 |
| wbw.framework.rocketmq.name-server | localhost:9876 | RocketMQ 名称服务器 | 消息生产和消费 |
| wbw.framework.rocketmq.producer-group | ${spring.application.name}-producer | 生产者组 | 消息生产者标识 |

#### 3.1.8 RPC 模块配置

| 配置项 | 默认值 | 说明 | 使用场景 |
|-------|-------|------|----------|
| wbw.framework.dubbo.enabled | true | 是否启用 Dubbo | 控制 RPC 功能 |
| wbw.framework.dubbo.application-name | ${spring.application.name} | 应用名称 | Dubbo 服务标识 |
| wbw.framework.dubbo.registry-address | nacos://localhost:8848 | 注册中心地址 | 服务注册与发现 |

### 3.2 推荐最简配置方案

#### 3.2.1 基础配置示例
```yaml
spring:
  application:
    name: demo-service
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848

wbw:
  framework:
    security:
      jwt-secret: your-secret-key-here
```

#### 3.2.2 生产环境配置示例
```yaml
spring:
  application:
    name: demo-service
  profiles:
    active: prod
  cloud:
    nacos:
      discovery:
        server-addr: nacos-server:8848
      config:
        server-addr: nacos-server:8848

wbw:
  framework:
    security:
      jwt-secret: ${JWT_SECRET}
      token-expiration: 7200000
    redis:
      host: redis-server
      password: ${REDIS_PASSWORD}
    rocketmq:
      name-server: rocketmq-server:9876
```

### 3.3 配置管理最佳实践

1. **敏感配置加密**：生产环境中的敏感配置（如密码、密钥）应使用环境变量或配置中心加密
2. **环境隔离**：使用不同的配置文件区分开发、测试、生产环境
3. **配置版本控制**：将配置文件纳入版本控制系统
4. **配置验证**：启动时验证配置的合法性和完整性
5. **动态配置**：使用 Nacos 等配置中心实现配置的动态更新

## 4. 微服务项目集成指南

### 4.1 框架安装

#### 4.1.1 安装命令
```bash
# 在微服务项目的根目录执行
mvn dependency:copy -Dartifact=com.wbw:wbw-starter:1.0.0:jar -DoutputDirectory=lib
```

#### 4.1.2 详细步骤

1. **添加依赖**：在微服务项目的 `pom.xml` 中添加框架依赖
2. **配置环境**：设置必要的环境变量
3. **初始化配置**：创建基础配置文件
4. **启动服务**：验证服务是否正常启动

### 4.2 微服务架构兼容配置

#### 4.2.1 服务注册与发现
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR}
        namespace: ${NACOS_NAMESPACE}
        group: ${SERVICE_GROUP}
```

#### 4.2.2 配置中心
```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: ${NACOS_SERVER_ADDR}
        namespace: ${NACOS_NAMESPACE}
        group: ${CONFIG_GROUP}
        file-extension: yaml
```

#### 4.2.3 负载均衡
```yaml
wbw:
  framework:
    dubbo:
      cluster: failover
      retries: 2
      loadbalance: random
```

#### 4.2.4 熔断降级
```yaml
resilience4j:
  circuitbreaker:
    instances:
      default:
        registerHealthIndicator: true
        slidingWindowSize: 100
        minimumNumberOfCalls: 10
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
```

### 4.3 常见集成问题及解决方案

#### 4.3.1 依赖冲突

**问题**：框架依赖与项目依赖版本冲突

**解决方案**：
- 使用 `mvn dependency:tree` 分析依赖树
- 在 `pom.xml` 中通过 `<exclusions>` 排除冲突依赖
- 确保使用统一的依赖版本管理

#### 4.3.2 配置覆盖

**问题**：项目配置被框架默认配置覆盖

**解决方案**：
- 了解配置优先级：命令行参数 > 环境变量 > 配置文件
- 在项目配置文件中明确指定配置项
- 检查配置文件加载顺序

#### 4.3.3 服务启动失败

**问题**：集成框架后服务无法正常启动

**解决方案**：
- 检查日志中的错误信息
- 验证依赖是否正确引入
- 确认配置项是否合法
- 检查端口是否被占用

#### 4.3.4 性能问题

**问题**：集成框架后服务性能下降

**解决方案**：
- 检查自动配置是否全部必要
- 优化缓存策略
- 调整线程池配置
- 监控系统资源使用情况

#### 4.3.5 安全问题

**问题**：框架集成后存在安全隐患

**解决方案**：
- 定期更新框架版本
- 配置适当的安全策略
- 启用 HTTPS
- 实现合理的认证授权机制

## 5. 最佳实践

### 5.1 微服务设计最佳实践

1. **服务划分**：基于业务能力划分微服务，保持服务边界清晰
2. **API 设计**：遵循 RESTful 设计规范，使用合适的 HTTP 方法和状态码
3. **数据管理**：每个微服务使用独立的数据库，避免跨服务直接访问数据
4. **异步通信**：使用消息队列实现服务间的异步通信
5. **容错设计**：实现熔断、降级、重试等容错机制

### 5.2 开发最佳实践

1. **代码规范**：遵循 Java 代码规范，使用 Lombok 简化代码
2. **测试覆盖**：编写单元测试和集成测试，确保代码质量
3. **日志管理**：使用统一的日志格式，实现日志链路追踪
4. **监控告警**：集成 Prometheus 和 Grafana，实现系统监控
5. **CI/CD**：建立持续集成和持续部署流程

### 5.3 部署最佳实践

1. **容器化**：使用 Docker 容器化部署微服务
2. **编排管理**：使用 Kubernetes 管理容器集群
3. **环境一致性**：确保开发、测试、生产环境配置一致
4. **滚动更新**：实现服务的滚动更新，减少 downtime
5. **备份恢复**：定期备份数据，制定灾难恢复计划

## 6. 故障排查指南

### 6.1 常见错误及解决方法

#### 6.1.1 启动错误

**错误信息**：`Failed to start bean 'webServerStartStop'`

**可能原因**：
- 端口被占用
- 配置文件错误
- 依赖缺失

**解决方法**：
- 检查端口使用情况：`netstat -ano | findstr :8080`
- 验证配置文件格式和内容
- 确保所有依赖正确引入

#### 6.1.2 服务调用错误

**错误信息**：`No provider available for service`

**可能原因**：
- 服务未注册到注册中心
- 网络连接问题
- 服务下线

**解决方法**：
- 检查服务是否正常注册
- 验证网络连通性
- 查看服务健康状态

#### 6.1.3 数据库错误

**错误信息**：`Could not get JDBC Connection`

**可能原因**：
- 数据库连接配置错误
- 数据库服务不可用
- 连接池耗尽

**解决方法**：
- 验证数据库连接配置
- 检查数据库服务状态
- 调整连接池参数

#### 6.1.4 缓存错误

**错误信息**：`Redis connection timed out`

**可能原因**：
- Redis 服务不可用
- 网络连接问题
- 缓存键过期策略不当

**解决方法**：
- 检查 Redis 服务状态
- 验证网络连通性
- 优化缓存策略

### 6.2 日志分析

1. **日志级别**：根据环境调整日志级别，生产环境建议使用 INFO 或 WARN
2. **日志格式**：使用结构化日志格式，便于日志分析工具处理
3. **日志聚合**：使用 ELK 或 Loki 等工具聚合分布式日志
4. **日志查询**：通过 traceId 查询完整的请求链路

### 6.3 性能分析

1. **监控指标**：关注响应时间、吞吐量、错误率等关键指标
2. **性能测试**：定期进行性能测试，建立性能基准
3. **瓶颈定位**：使用 Arthas 等工具定位性能瓶颈
4. **优化策略**：根据性能分析结果制定优化策略

## 7. 版本管理

### 7.1 版本号规则
采用语义化版本号：`X.Y.Z`
- **X**：主版本号，不兼容的 API 变更
- **Y**：次版本号，向下兼容的功能新增
- **Z**：修订版本号，向下兼容的问题修正

### 7.2 版本发布流程
1. **开发阶段**：`X.Y.Z-SNAPSHOT`
2. **测试阶段**：`X.Y.Z-RC.N`
3. **发布阶段**：`X.Y.Z`

### 7.3 版本兼容性
- 主版本号变更：可能不兼容
- 次版本号变更：向下兼容
- 修订版本号变更：完全兼容

## 8. 贡献指南

### 8.1 代码贡献
1. **Fork 项目**：在 GitHub 上 Fork 项目到个人账号
2. **创建分支**：基于 develop 分支创建功能分支
3. **提交代码**：提交代码并编写测试
4. **发起 PR**：向主仓库发起 Pull Request
5. **代码审查**：等待维护者代码审查
6. **合并代码**：审查通过后合并到主分支

### 8.2 文档贡献
1. **发现问题**：发现文档中的错误或遗漏
2. **修改文档**：更新文档内容
3. **提交 PR**：提交文档修改

### 8.3 问题反馈
1. **Bug 报告**：在 GitHub Issues 中提交 Bug 报告
2. **功能请求**：提出新功能或改进建议
3. **讨论交流**：参与项目讨论和技术交流

## 9. 许可证

本项目采用 Apache 2.0 许可证，详见 LICENSE 文件。

## 10. 联系方式

- **项目地址**：<项目 GitHub 地址>
- **文档地址**：<项目文档地址>
- **邮件列表**：<邮件列表地址>
- **Issue 追踪**：<Issue 地址>

---

**© 2026 WBW Team. All rights reserved.**