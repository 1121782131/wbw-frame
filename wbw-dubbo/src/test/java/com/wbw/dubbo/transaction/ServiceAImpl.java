package com.wbw.dubbo.transaction;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试服务A实现类
 */
@Service
public class ServiceAImpl implements ServiceA {

    @Autowired
    private ServiceB serviceB;

    @Autowired
    private TransactionLogger transactionLogger;

    @Override
    @GlobalTransactional
    public void doBusiness(String username, double amount) {
        String xid = TransactionContextHolder.getXid();
        transactionLogger.logTransactionStart("ServiceA.doBusiness", xid);

        try {
            System.out.println("ServiceA: 开始执行业务逻辑，用户：" + username + "，金额：" + amount);
            
            // 调用ServiceB的方法
            serviceB.processOrder(username, amount);
            
            // 调用ServiceC的方法
            // serviceC.doSomething();
            
            transactionLogger.logTransactionEnd("ServiceA.doBusiness", xid, "SUCCESS");
        } catch (Exception e) {
            transactionLogger.logTransactionError("ServiceA.doBusiness", xid, e.getMessage());
            transactionLogger.logTransactionEnd("ServiceA.doBusiness", xid, "FAILED");
            throw e;
        }
    }

    @Override
    public boolean doNonTransactionalBusiness(String username) {
        System.out.println("ServiceA: 执行非事务业务逻辑，用户：" + username);
        
        // 直接调用ServiceB的方法，不通过事务
        return serviceB.processNonTransactionalOrder(username);
    }
}
