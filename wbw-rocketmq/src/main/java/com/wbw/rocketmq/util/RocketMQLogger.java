package com.wbw.rocketmq.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RocketMQ日志工具类
 * 提供消息发送、消费、异常和性能指标的日志记录功能
 */
@Slf4j
public class RocketMQLogger {

    private static final String MSG_PREFIX = "[RocketMQ] ";

    /**
     * 记录消息发送开始日志
     * @param topic 主题
     * @param tag 标签
     * @param messageId 消息ID
     * @param message 消息内容
     */
    public static void logSendStart(String topic, String tag, String messageId, Object message) {
        log.info("{}Sending message - Topic: {}, Tag: {}, MessageId: {}, Content: {}", 
                MSG_PREFIX, topic, tag, messageId, truncateMessage(message));
    }

    /**
     * 记录消息发送成功日志
     * @param topic 主题
     * @param tag 标签
     * @param sendResult 发送结果
     * @param costTime 耗时（毫秒）
     */
    public static void logSendSuccess(String topic, String tag, SendResult sendResult, long costTime) {
        log.info("{}Message sent successfully - Topic: {}, Tag: {}, SendResult: {}, CostTime: {}ms", 
                MSG_PREFIX, topic, tag, sendResult, costTime);
    }

    /**
     * 记录消息发送失败日志
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param exception 异常
     */
    public static void logSendFailure(String topic, String tag, Object message, Exception exception) {
        log.error("{}Failed to send message - Topic: {}, Tag: {}, Content: {}, Error: {}", 
                MSG_PREFIX, topic, tag, truncateMessage(message), exception.getMessage(), exception);
    }

    /**
     * 记录消息消费开始日志
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageExt 消息
     */
    public static void logConsumeStart(String topic, String tag, String consumerGroup, MessageExt messageExt) {
        log.info("{}Consuming message - Topic: {}, Tag: {}, ConsumerGroup: {}, MessageId: {}, Keys: {}, Content: {}", 
                MSG_PREFIX, topic, tag, consumerGroup, messageExt.getMsgId(), messageExt.getKeys(), 
                truncateMessage(new String(messageExt.getBody())));
    }

    /**
     * 记录消息消费成功日志
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageId 消息ID
     * @param costTime 耗时（毫秒）
     */
    public static void logConsumeSuccess(String topic, String tag, String consumerGroup, String messageId, long costTime) {
        log.info("{}Message consumed successfully - Topic: {}, Tag: {}, ConsumerGroup: {}, MessageId: {}, CostTime: {}ms", 
                MSG_PREFIX, topic, tag, consumerGroup, messageId, costTime);
    }

    /**
     * 记录消息消费失败日志
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageId 消息ID
     * @param exception 异常
     */
    public static void logConsumeFailure(String topic, String tag, String consumerGroup, String messageId, Exception exception) {
        log.error("{}Failed to consume message - Topic: {}, Tag: {}, ConsumerGroup: {}, MessageId: {}, Error: {}", 
                MSG_PREFIX, topic, tag, consumerGroup, messageId, exception.getMessage(), exception);
    }

    /**
     * 记录批量消息消费开始日志
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageCount 消息数量
     */
    public static void logBatchConsumeStart(String topic, String tag, String consumerGroup, int messageCount) {
        log.info("{}Consuming batch messages - Topic: {}, Tag: {}, ConsumerGroup: {}, MessageCount: {}", 
                MSG_PREFIX, topic, tag, consumerGroup, messageCount);
    }

    /**
     * 记录批量消息消费完成日志
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageCount 消息数量
     * @param successCount 成功数量
     * @param failureCount 失败数量
     * @param costTime 耗时（毫秒）
     */
    public static void logBatchConsumeComplete(String topic, String tag, String consumerGroup, 
                                             int messageCount, int successCount, int failureCount, long costTime) {
        log.info("{}Batch message consumption completed - Topic: {}, Tag: {}, ConsumerGroup: {}, " +
                        "MessageCount: {}, SuccessCount: {}, FailureCount: {}, CostTime: {}ms", 
                MSG_PREFIX, topic, tag, consumerGroup, messageCount, successCount, failureCount, costTime);
    }

    /**
     * 记录事务消息状态日志
     * @param topic 主题
     * @param tag 标签
     * @param messageId 消息ID
     * @param transactionId 事务ID
     * @param status 状态
     */
    public static void logTransactionStatus(String topic, String tag, String messageId, 
                                          String transactionId, String status) {
        log.info("{}Transaction message status - Topic: {}, Tag: {}, MessageId: {}, TransactionId: {}, Status: {}", 
                MSG_PREFIX, topic, tag, messageId, transactionId, status);
    }

    /**
     * 记录性能指标日志
     * @param operation 操作类型
     * @param topic 主题
     * @param metrics 指标
     */
    public static void logMetrics(String operation, String topic, Map<String, Object> metrics) {
        log.info("{}Performance metrics - Operation: {}, Topic: {}, Metrics: {}", 
                MSG_PREFIX, operation, topic, metrics);
    }

    /**
     * 记录消费者状态变更日志
     * @param consumerGroup 消费者组
     * @param topic 主题
     * @param status 状态
     */
    public static void logConsumerStatusChange(String consumerGroup, String topic, String status) {
        log.info("{}Consumer status changed - ConsumerGroup: {}, Topic: {}, Status: {}", 
                MSG_PREFIX, consumerGroup, topic, status);
    }

    /**
     * 截断消息内容，避免日志过大
     * @param message 消息内容
     * @return 截断后的消息内容
     */
    private static String truncateMessage(Object message) {
        if (message == null) {
            return "null";
        }
        String messageStr = message.toString();
        int maxLength = 500;
        if (messageStr.length() <= maxLength) {
            return messageStr;
        }
        return messageStr.substring(0, maxLength) + "... (truncated)";
    }

    /**
     * 计算耗时
     * @param startTime 开始时间
     * @return 耗时（毫秒）
     */
    public static long calculateCostTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 计算耗时
     * @param startTime 开始时间
     * @param timeUnit 时间单位
     * @return 耗时
     */
    public static long calculateCostTime(long startTime, TimeUnit timeUnit) {
        long costTimeMs = System.currentTimeMillis() - startTime;
        return timeUnit.convert(costTimeMs, TimeUnit.MILLISECONDS);
    }
}
