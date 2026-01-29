package com.wbw.dubbo.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事务日志记录器
 * 用于记录事务传播相关的日志，便于问题排查和追踪
 */
public class TransactionLogger {

    private static final Logger logger = LoggerFactory.getLogger(TransactionLogger.class);

    /**
     * 记录事务开始日志
     * @param methodName 方法名
     * @param xid 事务XID
     */
    public static void logTransactionStart(String methodName, String xid) {
        logger.info("事务开始：方法={}，XID={}", methodName, xid);
    }

    /**
     * 记录事务结束日志
     * @param methodName 方法名
     * @param xid 事务XID
     * @param status 事务状态
     */
    public static void logTransactionEnd(String methodName, String xid, String status) {
        logger.info("事务结束：方法={}，XID={}，状态={}", methodName, xid, status);
    }

    /**
     * 记录事务传播日志
     * @param fromMethod 来源方法
     * @param toMethod 目标方法
     * @param xid 事务XID
     */
    public static void logTransactionPropagate(String fromMethod, String toMethod, String xid) {
        logger.info("事务传播：从方法={} 到方法={}，XID={}", fromMethod, toMethod, xid);
    }

    /**
     * 记录事务异常日志
     * @param methodName 方法名
     * @param xid 事务XID
     * @param errorMessage 错误消息
     */
    public static void logTransactionError(String methodName, String xid, String errorMessage) {
        logger.error("事务异常：方法={}，XID={}，错误={}", methodName, xid, errorMessage);
    }

    /**
     * 记录事务补偿日志
     * @param transactionId 事务ID
     * @param status 补偿状态
     * @param message 补偿消息
     */
    public static void logTransactionCompensate(String transactionId, String status, String message) {
        logger.info("事务补偿：事务ID={}，状态={}，消息={}", transactionId, status, message);
    }

    /**
     * 记录事务超时日志
     * @param methodName 方法名
     * @param xid 事务XID
     * @param timeout 超时时间（毫秒）
     */
    public static void logTransactionTimeout(String methodName, String xid, long timeout) {
        logger.warn("事务超时：方法={}，XID={}，超时时间={}ms", methodName, xid, timeout);
    }
}
