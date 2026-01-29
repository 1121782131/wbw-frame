package com.wbw.dubbo.transaction;

/**
 * 测试服务B接口
 */
public interface ServiceB {

    /**
     * 处理订单，包含事务
     * @param username 用户名
     * @param amount 金额
     */
    void processOrder(String username, double amount);

    /**
     * 处理订单，不包含事务
     * @param username 用户名
     * @return 是否成功
     */
    boolean processNonTransactionalOrder(String username);
}
