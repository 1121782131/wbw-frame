package com.wbw.dubbo.transaction;

/**
 * 测试服务A接口
 */
public interface ServiceA {

    /**
     * 执行业务逻辑，包含事务
     * @param username 用户名
     * @param amount 金额
     */
    void doBusiness(String username, double amount);

    /**
     * 执行业务逻辑，不包含事务
     * @param username 用户名
     * @return 是否成功
     */
    boolean doNonTransactionalBusiness(String username);
}
