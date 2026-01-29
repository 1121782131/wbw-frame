package com.wbw.dubbo.transaction;

/**
 * 订单服务
 */
public interface OrderService {

    /**
     * 创建订单
     * @param username 用户名
     * @param amount 金额
     */
    void createOrder(String username, double amount);

    /**
     * 根据用户名检查订单是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}
