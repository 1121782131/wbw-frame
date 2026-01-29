package com.wbw.dubbo;

import com.wbw.dubbo.service.UserService;
import com.wbw.dubbo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务压力测试
 */
@SpringBootTest
public class UserServiceStressTest {

    /**
     * 模拟用户服务实现
     */
    @MockBean
    private UserServiceImpl userService;

    /**
     * 并发线程数
     */
    private static final int THREAD_COUNT = 100;

    /**
     * 每个线程的请求数
     */
    private static final int REQUEST_PER_THREAD = 10;

    /**
     * 测试高并发场景下的服务性能
     */
    @Test
    public void testHighConcurrency() throws InterruptedException {
        // 模拟服务提供者的行为
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn("User{id=1, name=\"test_user\", age=25}");
        Mockito.when(userService.testService(Mockito.anyString())).thenReturn("服务响应: Hello Dubbo!");

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 执行并发请求
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < REQUEST_PER_THREAD; j++) {
                        // 测试getUserById方法
                        userService.getUserById(1L);

                        // 测试testService方法
                        userService.testService("Hello from thread " + threadId);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        // 等待所有线程执行完成
        countDownLatch.await(60, TimeUnit.SECONDS);

        // 记录结束时间
        long endTime = System.currentTimeMillis();

        // 关闭线程池
        executorService.shutdown();

        // 计算总请求数和执行时间
        int totalRequests = THREAD_COUNT * REQUEST_PER_THREAD * 2; // 每个线程执行2个方法
        long executionTime = endTime - startTime;

        // 输出测试结果
        System.out.println("压力测试结果：");
        System.out.println("并发线程数: " + THREAD_COUNT);
        System.out.println("每个线程请求数: " + REQUEST_PER_THREAD);
        System.out.println("总请求数: " + totalRequests);
        System.out.println("执行时间: " + executionTime + "ms");
        System.out.println("平均响应时间: " + (double) executionTime / totalRequests + "ms");
        System.out.println("QPS: " + (double) totalRequests * 1000 / executionTime);

        // 验证所有请求都被处理
        Mockito.verify(userService, Mockito.times(totalRequests / 2)).getUserById(Mockito.anyLong());
        Mockito.verify(userService, Mockito.times(totalRequests / 2)).testService(Mockito.anyString());
    }
}
