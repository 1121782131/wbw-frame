# wbw-common 模块技术使用文档

## 1. 模块概述

`wbw-common` 模块是企业级微服务框架的基础公共模块，提供了全系统通用的工具类、常量定义、异常处理和统一返回结果等核心功能。该模块设计目标是为整个微服务架构提供标准化的基础组件，确保系统各模块间的一致性和可维护性。

**核心功能：**
- 统一返回结果封装
- 标准化异常体系
- 通用工具类集合
- 业务常量定义

**应用场景：**
- 所有微服务模块的基础依赖
- API接口响应格式化
- 系统异常处理
- 日常开发中的通用工具方法调用

## 2. 接口说明

### 2.1 统一返回结果接口

| 接口名称 | 功能描述 |
|---------|--------|
| `Result.success()` | 成功返回，无数据 |
| `Result.success(T data)` | 成功返回，带数据 |
| `Result.success(String message, T data)` | 成功返回，带消息和数据 |
| `Result.error()` | 失败返回，默认消息 |
| `Result.error(String message)` | 失败返回，自定义消息 |
| `Result.error(Integer code, String message)` | 失败返回，自定义代码和消息 |
| `Result.error(Integer code, String message, T data)` | 失败返回，自定义代码、消息和数据 |
| `Result.judge(boolean flag)` | 根据布尔值返回结果 |
| `Result.judge(boolean flag, String successMsg, String errorMsg)` | 根据布尔值返回结果，带自定义消息 |

### 2.2 异常类接口

| 接口名称 | 功能描述 |
|---------|--------|
| `BusinessException` | 业务异常，用于业务逻辑错误 |
| `ServiceException` | 服务异常，用于服务层错误 |
| `PermissionException` | 权限异常，用于权限验证失败 |
| `SecurityException` | 安全异常，用于安全相关错误 |
| `SystemException` | 系统异常，用于系统级错误 |
| `TokenException` | 令牌异常，用于令牌验证失败 |
| `UserAuthenticationException` | 用户认证异常，用于用户登录验证失败 |
| `ValidationException` | 验证异常，用于参数验证失败 |

### 2.3 工具类接口

| 接口名称 | 功能描述 |
|---------|--------|
| `FrameDateUtil` | 日期时间处理工具 |
| `JsonUtil` | JSON序列化和反序列化工具 |
| `NumberUtil` | 数字处理工具 |
| `StringUtil` | 字符串处理工具 |
| `ExceptionUtil` | 异常处理工具 |

## 3. 参数规范

### 3.1 Result 类参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 描述 |
|---------|------|--------|---------|------|
| `code` | Integer | 是 | 任意整数 | 状态码 |
| `message` | String | 否 | 任意字符串 | 提示消息 |
| `data` | T | 否 | 任意类型 | 数据 |
| `success` | Boolean | 是 | true/false | 成功标志 |

### 3.2 异常类参数

| 参数名称 | 类型 | 必填性 | 取值范围 | 描述 |
|---------|------|--------|---------|------|
| `code` | Integer | 是 | 任意整数 | 异常代码 |
| `message` | String | 是 | 任意字符串 | 异常消息 |
| `cause` | Throwable | 否 | 任意异常 | 原始异常 |

## 4. 返回值定义

### 4.1 Result 类返回结构

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1620000000000,
  "traceId": null,
  "success": true
}
```

| 字段名称 | 类型 | 含义 |
|---------|------|------|
| `code` | Integer | 状态码 |
| `message` | String | 提示消息 |
| `data` | Object | 数据 |
| `timestamp` | Long | 时间戳 |
| `traceId` | String | 跟踪ID |
| `success` | Boolean | 成功标志 |

### 4.2 异常类返回结构

异常类通过全局异常处理器转换为统一的 Result 结构返回。

## 5. 使用示例

### 5.1 统一返回结果使用示例

```java
// 成功返回，无数据
return Result.success();

// 成功返回，带数据
User user = new User();
user.setId(1L);
user.setName("张三");
return Result.success(user);

// 成功返回，带自定义消息
return Result.success("用户创建成功", user);

// 失败返回，默认消息
return Result.error();

// 失败返回，自定义消息
return Result.error("用户名已存在");

// 失败返回，自定义代码和消息
return Result.error(400, "参数验证失败");

// 根据布尔值返回结果
boolean flag = userService.save(user);
return Result.judge(flag, "保存成功", "保存失败");
```

### 5.2 异常使用示例

```java
// 抛出业务异常
if (user == null) {
    throw new BusinessException(400, "用户不存在");
}

// 抛出权限异常
if (!hasPermission(userId, resourceId)) {
    throw new PermissionException(403, "无权限访问该资源");
}

// 抛出服务异常
try {
    // 业务逻辑
} catch (Exception e) {
    throw new ServiceException(500, "服务内部错误", e);
}
```

### 5.3 工具类使用示例

```java
// 日期工具使用
String now = FrameDateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
Date date = FrameDateUtil.parse("2023-01-01", "yyyy-MM-dd");

// JSON工具使用
String jsonStr = JsonUtil.toJson(user);
User user = JsonUtil.fromJson(jsonStr, User.class);

// 字符串工具使用
boolean isEmpty = StringUtil.isEmpty(str);
String trimmed = StringUtil.trim(str);

// 数字工具使用
boolean isNumber = NumberUtil.isNumber(str);
Integer num = NumberUtil.toInt(str);
```

## 6. 注意事项

### 6.1 性能考量

- **Result 对象创建**：每次接口响应都会创建 Result 对象，对于高并发场景，建议合理使用对象池或缓存机制。
- **异常抛出**：异常抛出会影响性能，应避免在正常业务流程中使用异常控制流程。
- **工具类方法**：部分工具方法可能涉及复杂计算，使用时应注意调用频率。

### 6.2 使用限制

- **异常体系**：应严格按照异常类型的语义使用，避免混用不同类型的异常。
- **返回结果**：所有API接口必须使用 Result 类封装返回结果，确保响应格式统一。
- **常量定义**：业务常量应统一在 BusinessConstant 中定义，避免散落各处。

### 6.3 常见问题解决方案

| 问题 | 原因 | 解决方案 |
|------|------|--------|
| Result 对象序列化失败 | 数据对象未实现 Serializable 接口 | 确保数据对象实现 Serializable 接口 |
| 异常信息丢失 | 异常链未正确传递 | 使用带 cause 参数的构造方法 |
| 工具方法返回值不符合预期 | 参数类型错误 | 检查参数类型是否匹配方法要求 |
| 状态码冲突 | 自定义状态码与系统状态码重叠 | 参考 BusinessConstant 中的状态码定义，使用合理范围的自定义码 |

### 6.4 最佳实践

- **统一异常处理**：结合全局异常处理器使用，避免在业务代码中重复捕获和处理异常。
- **链式调用**：Result 类支持链式调用，可根据需要灵活组合。
- **代码规范**：遵循项目代码规范，合理使用工具类，避免过度封装。
- **测试覆盖**：为工具类和核心方法编写单元测试，确保功能稳定性。