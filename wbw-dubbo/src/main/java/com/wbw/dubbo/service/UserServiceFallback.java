package com.wbw.dubbo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户服务降级类
 */
public class UserServiceFallback implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceFallback.class);

    @Override
    public String getUserById(Long id) {
        logger.warn("用户服务降级，返回默认用户信息，ID: {}", id);
        return "默认用户信息: {id=" + id + ", name=\"fallback_user\", age=0}";
    }

    @Override
    public Boolean createUser(String userInfo) {
        logger.warn("用户服务降级，返回默认创建结果，信息: {}", userInfo);
        return false;
    }

    @Override
    public Boolean updateUser(Long id, String userInfo) {
        logger.warn("用户服务降级，返回默认更新结果，ID: {}, 信息: {}", id, userInfo);
        return false;
    }

    @Override
    public Boolean deleteUser(Long id) {
        logger.warn("用户服务降级，返回默认删除结果，ID: {}", id);
        return false;
    }

    @Override
    public String testService(String message) {
        logger.warn("用户服务降级，返回默认测试结果，消息: {}", message);
        return "服务降级响应: " + message;
    }

    @Override
    public void createUserAndOrder(String username, double amount) {
        logger.warn("用户服务降级，返回默认创建用户和订单结果，用户名: {}, 金额: {}", username, amount);
        throw new RuntimeException("服务降级: 无法创建用户和订单");
    }

    @Override
    public boolean existsByUsername(String username) {
        logger.warn("用户服务降级，返回默认检查用户存在结果，用户名: {}", username);
        return false;
    }
}
