package com.wbw.dubbo.transaction;

/**
 * 事务补偿服务接口
 * 用于处理分布式事务中的异常情况，实现事务补偿机制
 */
public interface TransactionCompensateService {

    /**
     * 执行事务补偿
     * @param transactionId 事务ID
     * @param compensateData 补偿数据
     * @return 是否补偿成功
     */
    boolean compensate(String transactionId, String compensateData);

    /**
     * 记录事务补偿日志
     * @param transactionId 事务ID
     * @param compensateData 补偿数据
     * @param status 补偿状态
     * @param message 补偿消息
     */
    void recordCompensateLog(String transactionId, String compensateData, String status, String message);

    /**
     * 重试事务补偿
     * @param transactionId 事务ID
     * @param maxRetries 最大重试次数
     * @return 是否重试成功
     */
    boolean retryCompensate(String transactionId, int maxRetries);
}
