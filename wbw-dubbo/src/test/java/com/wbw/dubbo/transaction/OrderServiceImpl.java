package com.wbw.dubbo.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单服务实现
 */
@Service
public class OrderServiceImpl implements OrderService {

    // 模拟数据库
    private static final ConcurrentHashMap<String, Double> ORDERS = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public void createOrder(String username, double amount) {
        System.out.println("创建订单：" + username + " - " + amount);
        ORDERS.put(username, amount);
    }

    @Override
    public boolean existsByUsername(String username) {
        return ORDERS.containsKey(username);
    }

    /**
     * 清空所有订单（用于测试）
     */
    public void clearAll() {
        ORDERS.clear();
    }
}
