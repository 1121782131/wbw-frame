# RocketMQ客户端包

## 1. 模块介绍

RocketMQ客户端包是一个功能完整的RocketMQ集成解决方案，提供了简洁易用的API，支持消息发送、消费、事务消息、定时消息等功能，同时内置了错误处理、重试机制和日志记录功能。

### 核心功能

- **配置管理**：支持通过环境变量、配置文件或代码参数进行配置
- **生产者组件**：实现消息发送、事务消息、定时消息等功能
- **消费者组件**：支持集群消费和广播消费模式
- **错误处理与重试机制**：内置异常处理和重试策略
- **日志记录**：详细的消息发送和消费日志

## 2. 快速开始

### 2.1 依赖配置

在项目的pom.xml文件中添加以下依赖：

```xml
<dependency>
    <groupId>com.wbw</groupId>
    <artifactId>wbw-rocketmq</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2.2 配置文件

在application.yml文件中添加以下配置：

```yaml
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

### 2.3 发送消息示例

```java
import com.wbw.rocketmq.producer.RocketMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    @Autowired
    private RocketMQProducerService rocketMQProducerService;

    public void sendMessage() {
        // 发送同步消息
        rocketMQProducerService.send("test-topic", "Hello RocketMQ!");

        // 发送定时消息
        rocketMQProducerService.sendDelay("test-topic", "Delayed message", 3);

        // 发送事务消息
        rocketMQProducerService.sendTransaction("test-topic", "Transaction message", "transaction-arg");
    }
}
```

### 2.4 消费消息示例

```java
import com.wbw.rocketmq.consumer.RocketMQConsumerService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class MessageConsumerService {

    @Autowired
    private RocketMQConsumerService rocketMQConsumerService;

    @PostConstruct
    public void initConsumer() {
        try {
            // 注册集群消费者
            rocketMQConsumerService.registerClusterConsumer(
                    "test-topic",
                    "*",
                    "test-consumer-group",
                    new MessageListenerConcurrently() {
                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                            for (MessageExt msg : msgs) {
                                System.out.println("Received message: " + new String(msg.getBody()));
                            }
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 3. 配置说明

### 3.1 核心配置项

| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| rocketmq.name-server | String | localhost:9876 | NameServer地址 |
| rocketmq.producer.group | String | default-producer-group | 生产者组名 |
| rocketmq.producer.send-message-timeout | int | 3000 | 消息发送超时时间（毫秒） |
| rocketmq.producer.retry-times-when-send-failed | int | 2 | 同步发送消息失败重试次数 |
| rocketmq.consumer.group | String | default-consumer-group | 消费者组名 |
| rocketmq.consumer.consume-thread-min | int | 20 | 消费线程池最小大小 |
| rocketmq.consumer.consume-thread-max | int | 64 | 消费线程池最大大小 |
| rocketmq.consumer.message-model | String | CLUSTERING | 消费模式（CLUSTERING或BROADCASTING） |

### 3.2 高级配置项

| 配置项 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| rocketmq.producer.retry-times-when-send-async-failed | int | 2 | 异步发送消息失败重试次数 |
| rocketmq.producer.max-message-size | int | 4194304 | 消息体最大长度（字节） |
| rocketmq.producer.compress-message-body-threshold | int | 1048576 | 压缩消息阈值（字节） |
| rocketmq.consumer.pull-interval | int | 0 | 消息拉取间隔（毫秒） |
| rocketmq.consumer.pull-batch-size | int | 32 | 每次拉取的消息数 |
| rocketmq.consumer.consume-from-where | String | CONSUME_FROM_LAST_OFFSET | 消费起始位置 |
| rocketmq.consumer.consume-timeout | int | 15 | 消费超时时间（分钟） |

## 4. API文档

### 4.1 生产者服务API

#### 4.1.1 同步发送消息

```java
public <T> SendResult send(String topic, T message)
```

#### 4.1.2 异步发送消息

```java
public <T> void sendAsync(String topic, T message, SendCallback sendCallback)
```

#### 4.1.3 单向发送消息

```java
public <T> void sendOneWay(String topic, T message)
```

#### 4.1.4 发送定时消息

```java
public <T> SendResult sendDelay(String topic, T message, int delayTimeLevel)
```

#### 4.1.5 发送事务消息

```java
public <T, A> SendResult sendTransaction(String topic, T message, A arg)
```

### 4.2 消费者服务API

#### 4.2.1 注册集群消费者

```java
public void registerClusterConsumer(String topic, String tag, String consumerGroup, MessageListenerConcurrently messageListener) throws MQClientException
```

#### 4.2.2 注册广播消费者

```java
public void registerBroadcastConsumer(String topic, String tag, String consumerGroup, MessageListenerConcurrently messageListener) throws MQClientException
```

#### 4.2.3 注销消费者

```java
public void unregisterConsumer(String topic, String tag, String consumerGroup)
```

#### 4.2.4 暂停消费

```java
public void suspendConsumer(String topic, String tag, String consumerGroup)
```

#### 4.2.5 恢复消费

```java
public void resumeConsumer(String topic, String tag, String consumerGroup)
```

## 5. 错误处理

### 5.1 异常类型

- **RocketMQException**：RocketMQ通用异常
- **MQClientException**：MQ客户端异常
- **MQBrokerException**：MQ broker异常
- **RemotingException**：网络通信异常

### 5.2 重试机制

- **发送重试**：默认同步发送失败重试2次
- **消费重试**：消费失败后会自动重试，重试策略可配置
- **指数退避**：内置指数退避重试策略

## 6. 日志记录

### 6.1 日志级别

- **INFO**：消息发送和消费的基本信息
- **WARN**：警告信息，如重试操作
- **ERROR**：错误信息，如发送失败

### 6.2 日志内容

- **消息发送日志**：包括消息内容、发送结果、耗时等
- **消息消费日志**：包括消息内容、消费状态、耗时等
- **异常日志**：包括异常信息和堆栈跟踪
- **性能指标日志**：包括操作耗时等指标

## 7. 最佳实践

### 7.1 生产者最佳实践

1. **使用合适的发送方式**：
   - 重要消息使用同步发送
   - 非重要消息使用异步发送
   - 日志等消息使用单向发送

2. **合理设置重试次数**：根据业务重要性设置合适的重试次数

3. **使用事务消息**：对于需要保证消息发送和本地事务一致性的场景

4. **设置合理的超时时间**：根据网络情况设置合适的发送超时时间

### 7.2 消费者最佳实践

1. **选择合适的消费模式**：
   - 集群消费：适用于大多数场景
   - 广播消费：适用于消息需要发送给所有消费者的场景

2. **合理设置消费线程池大小**：根据消息处理耗时和并发需求设置

3. **实现幂等消费**：由于RocketMQ可能会重复投递消息，消费者需要实现幂等处理

4. **设置合理的消费超时时间**：根据消息处理耗时设置合适的超时时间

### 7.3 性能优化

1. **批量发送消息**：对于大量消息，使用批量发送可以提高性能

2. **合理设置批量拉取大小**：根据消息大小和处理能力设置合适的拉取大小

3. **使用异步消费**：对于耗时较长的消费操作，考虑使用异步处理

4. **监控消息队列**：定期监控消息队列的堆积情况，及时处理异常

## 8. 常见问题

### 8.1 消息发送失败

**可能原因**：
- NameServer地址配置错误
- Broker不可用
- 网络连接问题
- 消息大小超过限制

**解决方案**：
- 检查NameServer地址配置
- 检查Broker状态
- 检查网络连接
- 调整消息大小或使用压缩

### 8.2 消息消费失败

**可能原因**：
- 消费者处理逻辑异常
- 消费超时
- 消息格式错误

**解决方案**：
- 检查消费者处理逻辑
- 调整消费超时时间
- 检查消息格式

### 8.3 消息重复消费

**可能原因**：
- 消费者消费失败后重试
- 网络波动导致的重复投递

**解决方案**：
- 实现幂等消费
- 使用唯一消息ID进行去重

## 9. 版本说明

| 版本 | 说明 |
| --- | --- |
| 1.0.0 | 初始版本，实现了核心功能 |

## 10. 贡献指南

欢迎提交Issue和Pull Request，帮助改进这个项目。

## 11. 许可证

本项目采用Apache 2.0许可证。
