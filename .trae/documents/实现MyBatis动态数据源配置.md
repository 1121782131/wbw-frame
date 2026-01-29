# 实现MyBatis动态数据源配置

## 1. 扩展配置属性
- 扩展`MyBatisProperties`类，添加多数据源配置
- 新增`DataSourceProperties`类，用于配置单个数据源
- 新增`DynamicDataSourceProperties`类，用于管理多数据源

## 2. 实现动态数据源核心
- 创建`DynamicDataSource`类，继承`AbstractRoutingDataSource`
- 实现数据源路由逻辑，基于线程上下文或注解进行数据源切换

## 3. 数据源配置管理
- 创建`DynamicDataSourceConfig`类，负责初始化和管理多个数据源
- 支持主从数据源配置
- 支持数据源连接池配置

## 4. 数据源切换机制
- 实现`DataSourceContextHolder`类，用于管理线程级别的数据源上下文
- 提供`@DataSource`注解，用于在方法级别指定数据源
- 实现`DataSourceAspect`切面，处理数据源切换逻辑

## 5. 集成现有配置
- 修改`MyBatisAutoConfiguration`，根据配置决定是否启用动态数据源
- 确保与现有的MyBatis Plus配置和事务管理兼容

## 6. 配置示例
- 提供详细的配置示例，包括多数据源的yaml配置
- 提供使用示例，展示如何在代码中切换数据源

## 7. 测试和验证
- 编写测试用例，验证动态数据源的切换功能
- 测试主从数据源的自动切换
- 测试事务在不同数据源间的一致性

## 技术要点
- 基于Spring的`AbstractRoutingDataSource`实现数据源路由
- 使用线程本地变量存储当前数据源标识
- 通过AOP实现注解式数据源切换
- 支持动态数据源的运行时管理