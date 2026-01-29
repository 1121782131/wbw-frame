package com.wbw.rocketmq.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * RocketMQ异常处理器
 * 实现错误处理与重试机制
 */
@Slf4j
@Component
public class RocketMQExceptionHandler {

    /**
     * 处理RocketMQ异常
     * @param operation 操作
     * @param <T> 返回类型
     * @return 操作结果
     * @throws RocketMQException RocketMQ异常
     */
    public <T> T handle(Supplier<T> operation) throws RocketMQException {
        return handleWithRetry(operation, 1);
    }

    /**
     * 处理RocketMQ异常（带重试）
     * @param operation 操作
     * @param maxRetries 最大重试次数
     * @param <T> 返回类型
     * @return 操作结果
     * @throws RocketMQException RocketMQ异常
     */
    public <T> T handleWithRetry(Supplier<T> operation, int maxRetries) throws RocketMQException {
        int retries = 0;
        while (true) {
            try {
                return operation.get();
            } catch (Exception e) {
                retries++;
                if (retries > maxRetries) {
                    handleException(e);
                } else {
                    log.warn("Operation failed, retrying ({}/{})...: {}", retries, maxRetries, e.getMessage());
                    try {
                        // 指数退避策略
                        Thread.sleep(100 * (1 << (retries - 1)));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RocketMQException("Operation interrupted", ie);
                    }
                }
            }
        }
    }

    /**
     * 处理RocketMQ异常
     * @param e 异常
     * @throws RocketMQException RocketMQ异常
     */
    private void handleException(Exception e) throws RocketMQException {
        if (e instanceof MQClientException) {
            MQClientException mqce = (MQClientException) e;
            log.error("MQClientException: {} (error code: {})", mqce.getMessage(), mqce.getResponseCode());
            throw new RocketMQException(mqce.getMessage(), "MQ_CLIENT_ERROR", mqce);
        } else if (e instanceof MQBrokerException) {
            MQBrokerException mqbe = (MQBrokerException) e;
            log.error("MQBrokerException: {} (error code: {})", mqbe.getMessage(), mqbe.getResponseCode());
            throw new RocketMQException(mqbe.getMessage(), "MQ_BROKER_ERROR", mqbe);
        } else if (e instanceof RemotingException) {
            log.error("RemotingException: {}", e.getMessage());
            throw new RocketMQException(e.getMessage(), "REMOTING_ERROR", e);
        } else if (e instanceof RocketMQException) {
            throw (RocketMQException) e;
        } else {
            log.error("Unexpected exception: {}", e.getMessage(), e);
            throw new RocketMQException(e.getMessage(), "UNKNOWN_ERROR", e);
        }
    }

    /**
     * 处理无返回值的操作
     * @param operation 操作
     * @throws RocketMQException RocketMQ异常
     */
    public void handleRunnable(Runnable operation) throws RocketMQException {
        handle(() -> {
            operation.run();
            return null;
        });
    }

    /**
     * 处理无返回值的操作（带重试）
     * @param operation 操作
     * @param maxRetries 最大重试次数
     * @throws RocketMQException RocketMQ异常
     */
    public void handleRunnableWithRetry(Runnable operation, int maxRetries) throws RocketMQException {
        handleWithRetry(() -> {
            operation.run();
            return null;
        }, maxRetries);
    }
}
