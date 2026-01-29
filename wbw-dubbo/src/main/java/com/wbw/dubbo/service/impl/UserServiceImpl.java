package com.wbw.dubbo.service.impl;

import com.wbw.dubbo.service.UserService;
import com.wbw.dubbo.transaction.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户服务实现
 */
@DubboService(interfaceClass = UserService.class, version = "1.0.0", group = "wbw")
public class UserServiceImpl implements UserService {

    // 模拟数据库
    private static final ConcurrentHashMap<Long, String> USERS = new ConcurrentHashMap<>();
    private static long nextId = 1;

    @Autowired
    private OrderService orderService;

    @Override
    public String getUserById(Long id) {
        return USERS.get(id);
    }

    @Override
    public Boolean createUser(String userInfo) {
        Long id = nextId++;
        USERS.put(id, userInfo);
        return true;
    }

    @Override
    public Boolean updateUser(Long id, String userInfo) {
        if (USERS.containsKey(id)) {
            USERS.put(id, userInfo);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteUser(Long id) {
        return USERS.remove(id) != null;
    }

    @Override
    public String testService(String message) {
        return "Hello, " + message;
    }

    @Override
    @GlobalTransactional
    public void createUserAndOrder(String username, double amount) {
        System.out.println("创建用户：" + username);
        // 创建用户
        Long id = nextId++;
        USERS.put(id, username);
        
        // 创建订单
        System.out.println("创建订单：" + username + " - " + amount);
        orderService.createOrder(username, amount);
        
        // 模拟异常
        throw new RuntimeException("测试事务回滚");
    }

    @Override
    public boolean existsByUsername(String username) {
        return USERS.containsValue(username);
    }

    /**
     * 清空所有用户（用于测试）
     */
    public void clearAll() {
        USERS.clear();
        nextId = 1;
    }
}
