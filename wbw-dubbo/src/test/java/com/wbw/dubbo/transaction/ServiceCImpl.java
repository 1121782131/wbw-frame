package com.wbw.dubbo.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务C实现
 */
@Service
public class ServiceCImpl implements ServiceC {

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public void createOrder(String username) {
        System.out.println("ServiceC创建订单：" + username);
        // 创建订单
        orderService.createOrder(username, 100.0);
        // 模拟异常
        throw new RuntimeException("测试分布式事务边界");
    }
}
