package com.wbw.dubbo.transaction;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     * @param username 用户名
     * @param amount 金额
     */
    void createOrder(String username, double amount);
}
