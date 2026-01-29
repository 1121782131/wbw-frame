package com.wbw.dubbo.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试服务B实现类
 */
@Service
public class ServiceBImpl implements ServiceB {

    @Autowired
    private TransactionLogger transactionLogger;

    @Autowired
    private TransactionCompensateService transactionCompensateService;

    @Override
    public void processOrder(String username, double amount) {
        String xid = TransactionContextHolder.getXid();
        transactionLogger.logTransactionStart("ServiceB.processOrder", xid);

        try {
            System.out.println("ServiceB: 开始处理订单，用户：" + username + "，金额：" + amount);
            
            // 模拟订单处理逻辑
            System.out.println("ServiceB: 处理订单中...");
            
            // 模拟异常，测试事务回滚
            throw new RuntimeException("ServiceB: 模拟订单处理异常");
            
            // transactionLogger.logTransactionEnd("ServiceB.processOrder", xid, "SUCCESS");
        } catch (Exception e) {
            transactionLogger.logTransactionError("ServiceB.processOrder", xid, e.getMessage());
            transactionLogger.logTransactionEnd("ServiceB.processOrder", xid, "FAILED");
            
            // 记录事务补偿信息
            String compensateData = "{\"username\": \"" + username + "\", \"amount\": " + amount + ", \"action\": \"rollback_order\"}";
            transactionCompensateService.recordCompensateLog(xid, compensateData, "NEEDED", "订单处理失败，需要补偿");
            
            throw e;
        }
    }

    @Override
    public boolean processNonTransactionalOrder(String username) {
        System.out.println("ServiceB: 开始处理非事务订单，用户：" + username);
        
        // 模拟订单处理逻辑
        System.out.println("ServiceB: 处理非事务订单中...");
        
        return true;
    }
}
