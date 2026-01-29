package com.wbw.rocketmq.stress;

import com.wbw.rocketmq.config.RocketMQProperties;
import com.wbw.rocketmq.producer.RocketMQProducerService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * RocketMQ压力测试类
 * 模拟高并发场景下的消息发送和消费
 */
public class RocketMQStressTest {

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
        when(rocketMQTemplate.syncSend(anyString(), any())).thenReturn(mock(SendResult.class));
        when(rocketMQTemplate.syncSend(anyString(), any(), anyLong())).thenReturn(mock(SendResult.class));
        when(rocketMQTemplate.syncSend(anyString(), any(), anyLong(), anyInt())).thenReturn(mock(SendResult.class));
        
        rocketMQProducerService = new RocketMQProducerService(rocketMQTemplate, rocketMQProperties);
    }

    /**
     * 测试高并发消息发送
     */
    @Test
    public void testHighConcurrencySend() throws InterruptedException {
        // 测试参数
        int threadCount = 20; // 并发线程数
        int messagesPerThread = 100; // 每个线程发送的消息数
        int totalMessages = threadCount * messagesPerThread;

        // 计数器
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(totalMessages);

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 执行并发发送
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                for (int j = 0; j < messagesPerThread; j++) {
                    try {
                        String topic = "stress-test-topic";
                        String message = "Stress test message from thread " + threadId + ", message " + j;
                        rocketMQProducerService.send(topic, message);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        System.err.println("Send failed: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        // 等待所有任务完成
        latch.await();

        // 结束时间
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // 输出结果
        System.out.println("=== RocketMQ 压力测试结果 ===");
        System.out.println("并发线程数: " + threadCount);
        System.out.println("每个线程消息数: " + messagesPerThread);
        System.out.println("总消息数: " + totalMessages);
        System.out.println("成功发送: " + successCount.get());
        System.out.println("失败发送: " + failureCount.get());
        System.out.println("耗时: " + elapsedTime + " ms");
        System.out.println("吞吐量: " + (totalMessages * 1000.0 / elapsedTime) + " 消息/秒");
        System.out.println("============================");

        // 验证结果
        assert failureCount.get() == 0 : "压力测试中存在失败的消息发送";

        // 关闭线程池
        executorService.shutdown();
    }

    /**
     * 测试高并发消息发送（带超时时间）
     */
    @Test
    public void testHighConcurrencySendWithTimeout() throws InterruptedException {
        // 测试参数
        int threadCount = 10;
        int messagesPerThread = 50;
        int totalMessages = threadCount * messagesPerThread;

        // 计数器
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(totalMessages);

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 执行并发发送
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                for (int j = 0; j < messagesPerThread; j++) {
                    try {
                        String topic = "stress-test-topic";
                        String message = "Stress test message with timeout from thread " + threadId + ", message " + j;
                        rocketMQProducerService.send(topic, message, java.time.Duration.ofSeconds(5));
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        System.err.println("Send with timeout failed: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        // 等待所有任务完成
        latch.await();

        // 结束时间
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // 输出结果
        System.out.println("=== RocketMQ 带超时压力测试结果 ===");
        System.out.println("并发线程数: " + threadCount);
        System.out.println("每个线程消息数: " + messagesPerThread);
        System.out.println("总消息数: " + totalMessages);
        System.out.println("成功发送: " + successCount.get());
        System.out.println("失败发送: " + failureCount.get());
        System.out.println("耗时: " + elapsedTime + " ms");
        System.out.println("吞吐量: " + (totalMessages * 1000.0 / elapsedTime) + " 消息/秒");
        System.out.println("================================");

        // 验证结果
        assert failureCount.get() == 0 : "带超时的压力测试中存在失败的消息发送";

        // 关闭线程池
        executorService.shutdown();
    }

    /**
     * 测试高并发延迟消息发送
     */
    @Test
    public void testHighConcurrencyDelaySend() throws InterruptedException {
        // 测试参数
        int threadCount = 10;
        int messagesPerThread = 50;
        int totalMessages = threadCount * messagesPerThread;

        // 计数器
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(totalMessages);

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 执行并发发送
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                for (int j = 0; j < messagesPerThread; j++) {
                    try {
                        String topic = "stress-test-delay-topic";
                        String message = "Stress test delay message from thread " + threadId + ", message " + j;
                        rocketMQProducerService.sendDelay(topic, message, 3); // 延迟级别3
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        System.err.println("Delay send failed: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        // 等待所有任务完成
        latch.await();

        // 结束时间
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // 输出结果
        System.out.println("=== RocketMQ 延迟消息压力测试结果 ===");
        System.out.println("并发线程数: " + threadCount);
        System.out.println("每个线程消息数: " + messagesPerThread);
        System.out.println("总消息数: " + totalMessages);
        System.out.println("成功发送: " + successCount.get());
        System.out.println("失败发送: " + failureCount.get());
        System.out.println("耗时: " + elapsedTime + " ms");
        System.out.println("吞吐量: " + (totalMessages * 1000.0 / elapsedTime) + " 消息/秒");
        System.out.println("==================================");

        // 验证结果
        assert failureCount.get() == 0 : "延迟消息压力测试中存在失败的消息发送";

        // 关闭线程池
        executorService.shutdown();
    }
}
