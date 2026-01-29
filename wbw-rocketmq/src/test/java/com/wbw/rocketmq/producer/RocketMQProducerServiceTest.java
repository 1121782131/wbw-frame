package com.wbw.rocketmq.producer;

import com.wbw.rocketmq.config.RocketMQProperties;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RocketMQ生产者服务测试类
 * 验证生产者服务的核心功能
 */
public class RocketMQProducerServiceTest {

    @Mock
    private RocketMQTemplate rocketMQTemplate;

    @Mock
    private RocketMQProperties rocketMQProperties;

    @Mock
    private RocketMQProperties.Producer producerProperties;

    private RocketMQProducerService rocketMQProducerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(rocketMQProperties.getProducer()).thenReturn(producerProperties);
        when(producerProperties.getSendMessageTimeout()).thenReturn(3000);
        rocketMQProducerService = new RocketMQProducerService(rocketMQTemplate, rocketMQProperties);
    }

    /**
     * 测试同步发送消息
     */
    @Test
    public void testSend() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用
        when(rocketMQTemplate.syncSend(eq(topic), eq(message))).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.send(topic, message);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).syncSend(eq(topic), eq(message));
    }

    /**
     * 测试同步发送消息（带标签）
     */
    @Test
    public void testSendWithTag() {
        // 准备测试数据
        String topic = "test-topic";
        String tag = "test-tag";
        String message = "test message";
        String destination = topic + ":" + tag;
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用
        when(rocketMQTemplate.syncSend(eq(destination), eq(message))).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.send(topic, tag, message);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).syncSend(eq(destination), eq(message));
    }

    /**
     * 测试同步发送消息（带超时时间）
     */
    @Test
    public void testSendWithTimeout() {
        // 准备测试数据
        String topic = "test-topic";
        Object message = "test message"; // 使用 Object 类型避免方法重载歧义
        Duration timeout = Duration.ofSeconds(5);
        long timeoutMillis = timeout.toMillis();
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用
        when(rocketMQTemplate.syncSend(eq(topic), eq(message), eq(timeoutMillis))).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.send(topic, message, timeout);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).syncSend(eq(topic), eq(message), eq(timeoutMillis));
    }

    /**
     * 测试异步发送消息
     */
    @Test
    public void testSendAsync() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        SendCallback sendCallback = mock(SendCallback.class);

        // 执行测试
        rocketMQProducerService.sendAsync(topic, message, sendCallback);

        // 验证结果
        verify(rocketMQTemplate, times(1)).asyncSend(eq(topic), eq(message), eq(sendCallback));
    }

    /**
     * 测试单向发送消息
     */
    @Test
    public void testSendOneWay() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";

        // 执行测试
        rocketMQProducerService.sendOneWay(topic, message);

        // 验证结果
        verify(rocketMQTemplate, times(1)).sendOneWay(eq(topic), eq(message));
    }

    /**
     * 测试发送延迟消息
     */
    @Test
    public void testSendDelay() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        int delayTimeLevel = 3;
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用 - 使用更通用的参数匹配
        when(rocketMQTemplate.syncSend(anyString(), any(Message.class), anyLong(), anyInt())).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.sendDelay(topic, message, delayTimeLevel);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).syncSend(anyString(), any(Message.class), anyLong(), anyInt());
    }

    /**
     * 测试发送延迟消息（带延迟时间）
     */
    @Test
    public void testSendDelayWithDuration() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        Duration delay = Duration.ofSeconds(10);
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用 - 使用更通用的参数匹配
        when(rocketMQTemplate.syncSend(anyString(), any(Message.class), anyLong(), anyInt())).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.sendDelay(topic, message, delay);

        // 验证结果
        assertNotNull(actualResult);
        verify(rocketMQTemplate, times(1)).syncSend(anyString(), any(Message.class), anyLong(), anyInt());
    }

    /**
     * 测试发送顺序消息
     */
    @Test
    public void testSendOrderly() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        String hashKey = "test-hash-key";
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用
        when(rocketMQTemplate.syncSendOrderly(eq(topic), eq(message), eq(hashKey))).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.sendOrderly(topic, message, hashKey);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).syncSendOrderly(eq(topic), eq(message), eq(hashKey));
    }

    /**
     * 测试发送事务消息
     */
    @Test
    public void testSendTransaction() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        Object arg = new Object();
        TransactionSendResult expectedResult = mock(TransactionSendResult.class);

        // 模拟方法调用
        when(rocketMQTemplate.sendMessageInTransaction(eq(topic), any(Message.class), eq(arg))).thenReturn(expectedResult);

        // 执行测试
        TransactionSendResult actualResult = rocketMQProducerService.sendTransaction(topic, message, arg);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).sendMessageInTransaction(eq(topic), any(Message.class), eq(arg));
    }

    /**
     * 测试发送带消息头的消息
     */
    @Test
    public void testSendWithHeaders() {
        // 准备测试数据
        String topic = "test-topic";
        String message = "test message";
        Map<String, Object> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        SendResult expectedResult = mock(SendResult.class);

        // 模拟方法调用
        when(rocketMQTemplate.syncSend(eq(topic), any(Message.class))).thenReturn(expectedResult);

        // 执行测试
        SendResult actualResult = rocketMQProducerService.sendWithHeaders(topic, message, headers);

        // 验证结果
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(rocketMQTemplate, times(1)).syncSend(eq(topic), any(Message.class));
    }
}
