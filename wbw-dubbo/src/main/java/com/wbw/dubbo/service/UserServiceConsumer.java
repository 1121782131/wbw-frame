package com.wbw.dubbo.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务消费者
 */
@Component
public class UserServiceConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceConsumer.class);

    /**
     * 引用用户服务
     */
    @DubboReference(
            interfaceClass = UserService.class,
            version = "1.0.0",
            group = "wbw",
            check = false,
            timeout = 5000,
            retries = 1,
            cluster = "failover",
            mock = "com.wbw.dubbo.service.UserServiceFallback"
    )
    private UserService userService;

    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    public String getUserInfo(Long id) {
        try {
            logger.info("调用用户服务获取用户信息，ID: {}", id);
            return userService.getUserById(id);
        } catch (Exception e) {
            logger.error("调用用户服务失败", e);
            return "获取用户信息失败: " + e.getMessage();
        }
    }

    /**
     * 创建用户
     * @param userInfo 用户信息
     * @return 创建结果
     */
    public Boolean createUserInfo(String userInfo) {
        try {
            logger.info("调用用户服务创建用户，信息: {}", userInfo);
            return userService.createUser(userInfo);
        } catch (Exception e) {
            logger.error("调用用户服务失败", e);
            return false;
        }
    }

    /**
     * 更新用户
     * @param id 用户ID
     * @param userInfo 用户信息
     * @return 更新结果
     */
    public Boolean updateUserInfo(Long id, String userInfo) {
        try {
            logger.info("调用用户服务更新用户，ID: {}, 信息: {}", id, userInfo);
            return userService.updateUser(id, userInfo);
        } catch (Exception e) {
            logger.error("调用用户服务失败", e);
            return false;
        }
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    public Boolean deleteUserInfo(Long id) {
        try {
            logger.info("调用用户服务删除用户，ID: {}", id);
            return userService.deleteUser(id);
        } catch (Exception e) {
            logger.error("调用用户服务失败", e);
            return false;
        }
    }

    /**
     * 测试服务调用
     * @param message 测试消息
     * @return 测试结果
     */
    public String testServiceCall(String message) {
        try {
            logger.info("调用用户服务测试，消息: {}", message);
            return userService.testService(message);
        } catch (Exception e) {
            logger.error("调用用户服务失败", e);
            return "测试服务失败: " + e.getMessage();
        }
    }
}
