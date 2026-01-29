package com.wbw.dubbo.transaction;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    // 模拟订单存储
    private static final Map<String, Double> ORDERS = new ConcurrentHashMap<>();

    @Override
    public void createOrder(String username, double amount) {
        System.out.println("OrderService: 创建订单，用户：" + username + "，金额：" + amount);
        
        // 模拟订单创建逻辑
        ORDERS.put(username, amount);
        
        System.out.println("OrderService: 订单创建成功，用户：" + username + "，金额：" + amount);
    }
}
