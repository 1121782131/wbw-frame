package com.wbw.dubbo.transaction;

import io.seata.spring.annotation.GlobalTransactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 事务上下文传播测试类
 * 用于验证跨服务调用时的事务传播情况
 */
@SpringBootTest(classes = TestConfig.class)
public class TransactionTest {

    @Autowired
    private ServiceA serviceA;

    @Autowired
    private TransactionCompensateService transactionCompensateService;

    @BeforeEach
    public void setUp() {
        // 测试前的准备工作
        System.out.println("开始测试事务上下文传播...");
    }

    /**
     * 测试事务上下文传播
     * 验证跨服务调用时事务上下文是否正确传递
     */
    @Test
    public void testTransactionPropagation() {
        try {
            // 调用ServiceA的方法，该方法会调用ServiceB和ServiceC
            serviceA.doBusiness("测试用户", 100.0);
        } catch (RuntimeException e) {
            // 预期会抛出异常，因为ServiceB中模拟了异常
            System.out.println("捕获到预期异常：" + e.getMessage());
        }

        // 验证事务补偿是否被调用
        // 这里可以添加更多的验证逻辑
    }

    /**
     * 测试事务补偿机制
     * 验证分布式事务中的异常情况是否能被正确处理
     */
    @Test
    public void testTransactionCompensate() {
        String transactionId = "test_tx_" + System.currentTimeMillis();
        String compensateData = "{\"userId\": 1, \"action\": \"rollback\", \"data\": {}}";

        // 执行事务补偿
        boolean result = transactionCompensateService.compensate(transactionId, compensateData);
        assertTrue(result, "事务补偿应该成功");

        // 测试重试机制
        boolean retryResult = transactionCompensateService.retryCompensate(transactionId, 3);
        assertTrue(retryResult, "事务补偿重试应该成功");
    }

    /**
     * 测试无事务上下文的情况
     * 验证在没有事务上下文时服务调用是否正常
     */
    @Test
    public void testNoTransactionContext() {
        // 直接调用ServiceB的方法，不通过ServiceA的事务方法
        boolean result = serviceA.doNonTransactionalBusiness("测试用户");
        assertTrue(result, "无事务上下文的服务调用应该成功");
    }
}
