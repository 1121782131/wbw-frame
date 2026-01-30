# wbw-security 模块技术使用文档

## 1. 模块概述

`wbw-security` 模块是企业级微服务框架的安全认证模块，基于 JWT（JSON Web Token）实现了完整的身份认证和授权体系。该模块设计目标是为微服务架构提供统一的安全认证机制，确保系统API接口的安全性和可追溯性。

**核心功能：**
- JWT令牌生成与验证
- 基于注解的权限控制
- 令牌黑名单管理
- 令牌自动清理
- 安全上下文管理

**应用场景：**
- 用户登录认证
- API接口权限控制
- 微服务间安全通信
- 令牌有效期管理

## 2. 接口说明

### 2.1 JWT令牌服务接口

| 接口名称 | 功能描述 |
|---------|--------|
| `JwtTokenService.generateToken(JwtUser user)` | 根据用户信息生成令牌 |
| `JwtTokenService.generateToken(TokenRequest request)` | 根据请求信息生成令牌 |
| `JwtTokenService.validateToken(String token)` | 验证令牌有效性 |
| `JwtTokenService.parseToken(String token)` | 解析令牌获取用户信息 |
| `JwtTokenService.refreshToken(String refreshToken)` | 刷新令牌 |
| `JwtTokenService.refreshToken(String refreshToken, JwtUser updatedUser)` | 刷新令牌并更新用户信息 |
| `JwtTokenService.extractToken(String authorizationHeader)` | 从请求头提取令牌 |
| `JwtTokenService.getTokenExpiration(String token)` | 获取令牌过期时间 |
| `JwtTokenService.addTokenToBlacklist(String token)` | 将令牌加入黑名单 |
| `JwtTokenService.isTokenInBlacklist(String token)` | 检查令牌是否在黑名单 |
| `JwtTokenService.removeTokenFromBlacklist(String token)` | 从黑名单移除令牌 |

### 2.2 安全注解接口

| 接口名称 | 功能描述 |
|---------|--------|
| `@Anonymous` | 标记接口为匿名访问，跳过认证 |
| `@JwtToken` | 标记接口需要JWT认证 |
| `@Whitelist` | 标记接口为白名单，跳过认证 |

### 2.3 安全工具接口

| 接口名称 | 功能描述 |
|---------|--------|
| `JwtUtil` | JWT令牌工具类 |
| `TokenHelper` | 令牌辅助工具类 |
| `JwtSecurityContext` | 安全上下文管理类 |

## 3. 参数规范

### 3.1 JwtUser 类参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 描述 |
|---------|------|--------|---------|------|
| `userId` | Long | 是 | 任意正数 | 用户ID |
| `username` | String | 是 | 非空字符串 | 用户名 |
| `nickname` | String | 否 | 任意字符串 | 用户昵称 |
| `roles` | List<String> | 否 | 角色列表 | 用户角色 |
| `permissions` | List<String> | 否 | 权限列表 | 用户权限 |
| `expireTime` | Long | 否 | 时间戳 | 过期时间 |
| `extraClaims` | Map<String, Object> | 否 | 任意键值对 | 额外声明 |

### 3.2 TokenRequest 类参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 描述 |
|---------|------|--------|---------|------|
| `username` | String | 是 | 非空字符串 | 用户名 |
| `password` | String | 是 | 非空字符串 | 密码 |
| `grantType` | String | 是 | "password", "refresh_token" | 授权类型 |
| `refreshToken` | String | 否 | 非空字符串 | 刷新令牌（grantType为refresh_token时必填） |
| `scope` | String | 否 | 任意字符串 | 权限范围 |

### 3.3 JwtProperties 配置参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 默认值 | 描述 |
|---------|------|--------|---------|-------|------|
| `enabled` | Boolean | 否 | true/false | true | 是否启用JWT |
| `secretKey` | String | 是 | 非空字符串 | - | 密钥 |
| `accessTokenExpireHours` | Integer | 否 | 正数 | 2 | 访问令牌过期时间（小时） |
| `refreshTokenExpireHours` | Integer | 否 | 正数 | 72 | 刷新令牌过期时间（小时） |
| `issuer` | String | 否 | 任意字符串 | "wbw-frame" | 令牌发行者 |
| `audience` | String | 否 | 任意字符串 | "wbw-app" | 令牌受众 |
| `blacklistEnabled` | Boolean | 否 | true/false | true | 是否启用黑名单 |
| `filter.enabled` | Boolean | 否 | true/false | false | 是否启用JWT过滤器 |
| `filter.urlPatterns` | String | 否 | URL模式 | "/*" | 过滤器URL模式 |

## 4. 返回值定义

### 4.1 TokenInfo 类返回结构

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 7200,
  "refreshExpiresIn": 259200,
  "scope": "read write",
  "userId": 1,
  "username": "admin"
}
```

| 字段名称 | 类型 | 含义 |
|---------|------|------|
| `accessToken` | String | 访问令牌 |
| `refreshToken` | String | 刷新令牌 |
| `tokenType` | String | 令牌类型 |
| `expiresIn` | Integer | 访问令牌过期时间（秒） |
| `refreshExpiresIn` | Integer | 刷新令牌过期时间（秒） |
| `scope` | String | 权限范围 |
| `userId` | Long | 用户ID |
| `username` | String | 用户名 |

### 4.2 JwtUser 类返回结构

```json
{
  "userId": 1,
  "username": "admin",
  "nickname": "管理员",
  "roles": ["ROLE_ADMIN", "ROLE_USER"],
  "permissions": ["user:read", "user:write"],
  "expireTime": 1620000000000,
  "extraClaims": {
    "department": "IT",
    "position": "Engineer"
  }
}
```

| 字段名称 | 类型 | 含义 |
|---------|------|------|
| `userId` | Long | 用户ID |
| `username` | String | 用户名 |
| `nickname` | String | 用户昵称 |
| `roles` | List<String> | 用户角色 |
| `permissions` | List<String> | 用户权限 |
| `expireTime` | Long | 过期时间 |
| `extraClaims` | Map<String, Object> | 额外声明 |

## 5. 使用示例

### 5.1 令牌生成与验证示例

```java
// 1. 注入JWT令牌服务
@Autowired
private JwtTokenService jwtTokenService;

// 2. 生成令牌
JwtUser user = new JwtUser();
user.setUserId(1L);
user.setUsername("admin");
user.setNickname("管理员");
user.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
user.setPermissions(Arrays.asList("user:read", "user:write"));

TokenInfo tokenInfo = jwtTokenService.generateToken(user);
System.out.println("访问令牌: " + tokenInfo.getAccessToken());
System.out.println("刷新令牌: " + tokenInfo.getRefreshToken());

// 3. 验证令牌
String token = tokenInfo.getAccessToken();
bboolean isValid = jwtTokenService.validateToken(token);
System.out.println("令牌是否有效: " + isValid);

// 4. 解析令牌
JwtUser parsedUser = jwtTokenService.parseToken(token);
System.out.println("解析出的用户: " + parsedUser.getUsername());

// 5. 刷新令牌
String refreshToken = tokenInfo.getRefreshToken();
TokenInfo newTokenInfo = jwtTokenService.refreshToken(refreshToken);
System.out.println("新访问令牌: " + newTokenInfo.getAccessToken());

// 6. 将令牌加入黑名单
jwtTokenService.addTokenToBlacklist(token);
bboolean isInBlacklist = jwtTokenService.isTokenInBlacklist(token);
System.out.println("令牌是否在黑名单: " + isInBlacklist);
```

### 5.2 注解使用示例

```java
// 1. 匿名访问接口
@Anonymous
@GetMapping("/login")
public Result<TokenInfo> login(@RequestBody TokenRequest request) {
    // 登录逻辑
    return Result.success(tokenInfo);
}

// 2. 需要JWT认证的接口
@JwtToken
@GetMapping("/user/info")
public Result<UserInfo> getUserInfo() {
    // 从安全上下文获取用户信息
    JwtUser user = JwtSecurityContext.getCurrentUser();
    // 业务逻辑
    return Result.success(userInfo);
}

// 3. 白名单接口
@Whitelist
@GetMapping("/health")
public Result<String> healthCheck() {
    return Result.success("ok");
}
```

### 5.3 安全上下文使用示例

```java
// 1. 设置当前用户
JwtUser user = jwtTokenService.parseToken(token);
JwtSecurityContext.setCurrentUser(user);

// 2. 获取当前用户
JwtUser currentUser = JwtSecurityContext.getCurrentUser();
System.out.println("当前用户: " + currentUser.getUsername());

// 3. 检查用户是否有某角色
boolean hasRole = JwtSecurityContext.hasRole("ROLE_ADMIN");
System.out.println("是否有管理员角色: " + hasRole);

// 4. 检查用户是否有某权限
boolean hasPermission = JwtSecurityContext.hasPermission("user:write");
System.out.println("是否有写用户权限: " + hasPermission);

// 5. 清除当前用户
JwtSecurityContext.clear();
```

## 6. 注意事项

### 6.1 性能考量

- **令牌验证**：每次API请求都会进行令牌验证，对于高并发场景，建议使用缓存机制减少令牌解析开销。
- **黑名单管理**：黑名单存储在Redis中，应合理设置过期时间，避免占用过多内存。
- **自动清理**：令牌自动清理任务会定期执行，应根据系统负载调整清理频率。
- **密钥管理**：密钥应通过环境变量或配置中心管理，避免硬编码。

### 6.2 使用限制

- **令牌长度**：JWT令牌包含用户信息，应避免在令牌中存储过多数据，导致令牌过长。
- **过期时间**：访问令牌过期时间不宜过长，建议设置为2小时以内，刷新令牌可适当延长。
- **权限粒度**：权限控制应根据业务需求合理设计，避免过度细化导致管理复杂。
- **安全传输**：令牌应通过HTTPS传输，避免在不安全的网络环境中被窃取。

### 6.3 常见问题解决方案

| 问题 | 原因 | 解决方案 |
|------|------|--------|
| 令牌验证失败 | 令牌已过期或被篡改 | 检查令牌过期时间，确保密钥一致性 |
| 安全上下文获取不到用户 | 过滤器未生效或令牌未解析 | 检查JwtAuthFilter配置，确保请求头包含正确的Authorization |
| 令牌黑名单不生效 | Redis连接失败或配置错误 | 检查Redis配置，确保黑名单功能正常启用 |
| 自动清理任务执行失败 | 调度器配置错误 | 检查SchedulingConfig配置，确保定时任务正常启动 |
| 注解权限控制不生效 | AOP切面未正确配置 | 检查AnonymousAspect配置，确保注解被正确扫描 |

### 6.4 最佳实践

- **分层认证**：在API网关层进行统一认证，微服务内部可简化认证逻辑。
- **令牌轮换**：定期轮换密钥，提高系统安全性。
- **监控告警**：对令牌验证失败、黑名单异常等情况进行监控和告警。
- **日志记录**：记录关键操作的令牌使用情况，便于审计和问题排查。
- **测试覆盖**：为令牌生成、验证、刷新等核心功能编写单元测试，确保安全性。
- **配置管理**：通过配置中心管理JWT相关配置，支持不同环境的灵活调整。