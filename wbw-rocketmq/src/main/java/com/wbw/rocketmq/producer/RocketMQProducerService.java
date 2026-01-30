package com.wbw.rocketmq.producer;

import com.wbw.rocketmq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

/**
 * RocketMQ生产者服务类
 * 实现消息发送、事务消息、定时消息等功能
 */
@Slf4j
@Service
public class RocketMQProducerService {

    private final RocketMQTemplate rocketMQTemplate;
    private final RocketMQProperties rocketMQProperties;

    public RocketMQProducerService(RocketMQTemplate rocketMQTemplate, RocketMQProperties rocketMQProperties) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.rocketMQProperties = rocketMQProperties;
    }

    /**
     * 同步发送消息
     * @param topic 主题
     * @param message 消息内容
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult send(String topic, T message) {
        log.info("Sending synchronous message to topic: {}", topic);
        SendResult sendResult = rocketMQTemplate.syncSend(topic, message);
        log.info("Synchronous message sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 同步发送消息（带标签）
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult send(String topic, String tag, T message) {
        String destination = topic + ":" + tag;
        log.info("Sending synchronous message to destination: {}", destination);
        SendResult sendResult = rocketMQTemplate.syncSend(destination, message);
        log.info("Synchronous message sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 同步发送消息（带超时时间）
     * @param topic 主题
     * @param message 消息内容
     * @param timeout 超时时间
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult sendWithTimeout(String topic, T message, Duration timeout) {
        log.info("Sending synchronous message to topic: {} with timeout: {}", topic, timeout);
        long timeoutMillis = timeout.toMillis();
        SendResult sendResult = rocketMQTemplate.syncSend(topic, message, timeoutMillis);
        log.info("Synchronous message sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 异步发送消息
     * @param topic 主题
     * @param message 消息内容
     * @param sendCallback 发送回调
     * @param <T> 消息类型
     */
    public <T> void sendAsync(String topic, T message, SendCallback sendCallback) {
        log.info("Sending asynchronous message to topic: {}", topic);
        rocketMQTemplate.asyncSend(topic, message, sendCallback);
        log.info("Asynchronous message sent (callback will be invoked)");
    }

    /**
     * 单向发送消息（不关心发送结果）
     * @param topic 主题
     * @param message 消息内容
     * @param <T> 消息类型
     */
    public <T> void sendOneWay(String topic, T message) {
        log.info("Sending one-way message to topic: {}", topic);
        rocketMQTemplate.sendOneWay(topic, message);
        log.info("One-way message sent");
    }

    /**
     * 发送定时消息
     * @param topic 主题
     * @param message 消息内容
     * @param delayTimeLevel 延迟级别（1-18）
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult sendDelay(String topic, T message, int delayTimeLevel) {
        log.info("Sending delay message to topic: {} with delay level: {}", topic, delayTimeLevel);
        Message<T> rocketMessage = MessageBuilder.withPayload(message).build();
        SendResult sendResult = rocketMQTemplate.syncSend(topic, rocketMessage, 
                rocketMQProperties.getProducer().getSendMessageTimeout(), delayTimeLevel);
        log.info("Delay message sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 发送定时消息（带延迟时间）
     * @param topic 主题
     * @param message 消息内容
     * @param delay 延迟时间
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult sendDelay(String topic, T message, Duration delay) {
        // 将Duration转换为延迟级别
        int delayTimeLevel = convertDurationToDelayLevel(delay);
        return sendDelay(topic, message, delayTimeLevel);
    }

    /**
     * 发送顺序消息
     * @param topic 主题
     * @param message 消息内容
     * @param hashKey 用于确定消息发送到哪个队列的关键字
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult sendOrderly(String topic, T message, String hashKey) {
        log.info("Sending orderly message to topic: {} with hashKey: {}", topic, hashKey);
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(topic, message, hashKey);
        log.info("Orderly message sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 发送事务消息
     * @param topic 主题
     * @param message 消息内容
     * @param arg 事务参数
     * @param <T> 消息类型
     * @param <A> 参数类型
     * @return 事务发送结果
     */
    public <T, A> TransactionSendResult sendTransaction(String topic, T message, A arg) {
        log.info("Sending transaction message to topic: {}", topic);
        Message<T> rocketMessage = MessageBuilder.withPayload(message).build();
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(topic, rocketMessage, arg);
        log.info("Transaction message sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 发送带消息头的消息
     * @param topic 主题
     * @param message 消息内容
     * @param headers 消息头
     * @param <T> 消息类型
     * @return 发送结果
     */
    public <T> SendResult sendWithHeaders(String topic, T message, Map<String, Object> headers) {
        log.info("Sending message with headers to topic: {}", topic);
        MessageBuilder<T> messageBuilder = MessageBuilder.withPayload(message);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(messageBuilder::setHeader);
        }
        Message<T> rocketMessage = messageBuilder.build();
        SendResult sendResult = rocketMQTemplate.syncSend(topic, rocketMessage);
        log.info("Message with headers sent: {}", sendResult);
        return sendResult;
    }

    /**
     * 将Duration转换为RocketMQ延迟级别
     * RocketMQ延迟级别对应关系：
     * 1s, 5s, 10s, 30s, 1m, 2m, 3m, 4m, 5m, 6m, 7m, 8m, 9m, 10m, 20m, 30m, 1h, 2h
     * @param delay 延迟时间
     * @return 延迟级别（1-18）
     */
    private int convertDurationToDelayLevel(Duration delay) {
        long seconds = delay.getSeconds();
        if (seconds <= 1) return 1;
        if (seconds <= 5) return 2;
        if (seconds <= 10) return 3;
        if (seconds <= 30) return 4;
        if (seconds <= 60) return 5;
        if (seconds <= 120) return 6;
        if (seconds <= 180) return 7;
        if (seconds <= 240) return 8;
        if (seconds <= 300) return 9;
        if (seconds <= 360) return 10;
        if (seconds <= 420) return 11;
        if (seconds <= 480) return 12;
        if (seconds <= 540) return 13;
        if (seconds <= 600) return 14;
        if (seconds <= 1200) return 15;
        if (seconds <= 1800) return 16;
        if (seconds <= 3600) return 17;
        return 18;
    }
}
