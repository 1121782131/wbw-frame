package com.wbw.dubbo.service;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    String getUserById(Long id);

    /**
     * 创建用户
     * @param userInfo 用户信息
     * @return 创建结果
     */
    Boolean createUser(String userInfo);

    /**
     * 更新用户
     * @param id 用户ID
     * @param userInfo 用户信息
     * @return 更新结果
     */
    Boolean updateUser(Long id, String userInfo);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    Boolean deleteUser(Long id);

    /**
     * 测试服务
     * @param message 测试消息
     * @return 测试结果
     */
    String testService(String message);
}
