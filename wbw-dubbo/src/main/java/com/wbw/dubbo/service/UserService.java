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
     * @return 是否成功
     */
    Boolean createUser(String userInfo);

    /**
     * 更新用户
     * @param id 用户ID
     * @param userInfo 用户信息
     * @return 是否成功
     */
    Boolean updateUser(Long id, String userInfo);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean deleteUser(Long id);

    /**
     * 测试服务
     * @param message 测试消息
     * @return 测试结果
     */
    String testService(String message);

    /**
     * 创建用户和订单（用于测试事务回滚）
     * @param username 用户名
     * @param amount 金额
     */
    void createUserAndOrder(String username, double amount);

    /**
     * 根据用户名检查用户是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}
