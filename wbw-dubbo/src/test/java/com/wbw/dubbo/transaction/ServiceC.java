package com.wbw.dubbo.transaction;

/**
 * 服务C
 */
public interface ServiceC {

    /**
     * 创建订单
     * @param username 用户名
     */
    void createOrder(String username);
}
