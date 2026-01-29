package com.wbw.rocketmq.consumer;

import com.wbw.rocketmq.config.RocketMQProperties;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RocketMQ消费者服务测试类
 * 验证消费者服务的核心功能
 */
public class RocketMQConsumerServiceTest {

    @Mock
    private RocketMQProperties rocketMQProperties;

    @Mock
    private RocketMQProperties.Consumer consumerProperties;

    @Mock
    private MessageListenerConcurrently messageListener;

    private RocketMQConsumerService rocketMQConsumerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(rocketMQProperties.getConsumer()).thenReturn(consumerProperties);
        when(consumerProperties.getConsumeThreadMin()).thenReturn(20);
        when(consumerProperties.getConsumeThreadMax()).thenReturn(64);
        when(consumerProperties.getPullInterval()).thenReturn(0);
        when(consumerProperties.getPullBatchSize()).thenReturn(32);
        when(rocketMQProperties.getNameServer()).thenReturn("localhost:9876");
        
        rocketMQConsumerService = new RocketMQConsumerService(rocketMQProperties);
    }

    /**
     * 测试获取未注册消费者的状态
     */
    @Test
    public void testGetConsumerStatusNotRegistered() {
        // 准备测试数据
        String topic = "test-topic";
        String tag = "*";
        String consumerGroup = "test-consumer-group";

        // 执行测试
        String status = rocketMQConsumerService.getConsumerStatus(topic, tag, consumerGroup);

        // 验证结果
        assertNotNull(status);
        assertEquals("NOT_REGISTERED", status);
    }

    /**
     * 测试关闭所有消费者
     */
    @Test
    public void testShutdownAllConsumers() {
        // 执行测试
        rocketMQConsumerService.shutdownAllConsumers();
        
        // 验证方法调用（无异常抛出）
        // 由于消费者列表为空，此方法应该正常执行而不抛出异常
    }

    /**
     * 测试注销不存在的消费者
     */
    @Test
    public void testUnregisterConsumerNotExist() {
        // 准备测试数据
        String topic = "test-topic";
        String tag = "*";
        String consumerGroup = "test-consumer-group";

        // 执行测试
        rocketMQConsumerService.unregisterConsumer(topic, tag, consumerGroup);
        
        // 验证方法调用（无异常抛出）
        // 由于消费者不存在，此方法应该正常执行而不抛出异常
    }

    /**
     * 测试暂停不存在的消费者
     */
    @Test
    public void testSuspendConsumerNotExist() {
        // 准备测试数据
        String topic = "test-topic";
        String tag = "*";
        String consumerGroup = "test-consumer-group";

        // 执行测试
        rocketMQConsumerService.suspendConsumer(topic, tag, consumerGroup);
        
        // 验证方法调用（无异常抛出）
        // 由于消费者不存在，此方法应该正常执行而不抛出异常
    }

    /**
     * 测试恢复不存在的消费者
     */
    @Test
    public void testResumeConsumerNotExist() {
        // 准备测试数据
        String topic = "test-topic";
        String tag = "*";
        String consumerGroup = "test-consumer-group";

        // 执行测试
        rocketMQConsumerService.resumeConsumer(topic, tag, consumerGroup);
        
        // 验证方法调用（无异常抛出）
        // 由于消费者不存在，此方法应该正常执行而不抛出异常
    }
}
