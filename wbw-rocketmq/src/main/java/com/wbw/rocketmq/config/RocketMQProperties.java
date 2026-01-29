package com.wbw.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RocketMQ配置属性类
 * 支持通过环境变量、配置文件或代码参数进行配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {

    /**
     * NameServer地址
     */
    private String nameServer = "localhost:9876";

    /**
     * 生产者配置
     */
    private Producer producer = new Producer();

    /**
     * 消费者配置
     */
    private Consumer consumer = new Consumer();

    /**
     * 生产者配置
     */
    @Data
    public static class Producer {
        /**
         * 生产者组名
         */
        private String group = "default-producer-group";

        /**
         * 消息发送超时时间（毫秒）
         */
        private int sendMessageTimeout = 3000;

        /**
         * 同步发送消息失败重试次数
         */
        private int retryTimesWhenSendFailed = 2;

        /**
         * 异步发送消息失败重试次数
         */
        private int retryTimesWhenSendAsyncFailed = 2;

        /**
         * 消息体最大长度（字节）
         */
        private int maxMessageSize = 1024 * 1024 * 4; // 4MB

        /**
         * 压缩消息阈值（字节）
         */
        private int compressMessageBodyThreshold = 1024 * 1024; // 1MB
    }

    /**
     * 消费者配置
     */
    @Data
    public static class Consumer {
        /**
         * 消费者组名
         */
        private String group = "default-consumer-group";

        /**
         * 消费线程池大小
         */
        private int consumeThreadMin = 20;

        /**
         * 消费线程池最大大小
         */
        private int consumeThreadMax = 64;

        /**
         * 消息拉取间隔（毫秒）
         */
        private int pullInterval = 0;

        /**
         * 每次拉取的消息数
         */
        private int pullBatchSize = 32;

        /**
         * 消费模式：CLUSTERING（集群消费）或BROADCASTING（广播消费）
         */
        private String messageModel = "CLUSTERING";

        /**
         * 消费起始位置：CONSUME_FROM_LAST_OFFSET（从最后位置开始消费）
         * CONSUME_FROM_FIRST_OFFSET（从第一个位置开始消费）
         * CONSUME_FROM_TIMESTAMP（从指定时间戳开始消费）
         */
        private String consumeFromWhere = "CONSUME_FROM_LAST_OFFSET";

        /**
         * 消费超时时间（分钟）
         */
        private int consumeTimeout = 15;
    }
}
