package com.wbw.rocketmq.consumer;

import com.wbw.rocketmq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RocketMQ消费者服务类
 * 支持集群消费和广播消费模式
 */
@Slf4j
@Service
public class RocketMQConsumerService {

    private final RocketMQProperties rocketMQProperties;
    private final Map<String, DefaultMQPushConsumer> consumers = new ConcurrentHashMap<>();

    public RocketMQConsumerService(RocketMQProperties rocketMQProperties) {
        this.rocketMQProperties = rocketMQProperties;
    }

    /**
     * 注册集群消费监听器
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageListener 消息监听器
     * @throws MQClientException MQ客户端异常
     */
    public void registerClusterConsumer(String topic, String tag, String consumerGroup, 
                                       MessageListenerConcurrently messageListener) throws MQClientException {
        registerConsumer(topic, tag, consumerGroup, messageListener, "CLUSTERING");
    }

    /**
     * 注册广播消费监听器
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageListener 消息监听器
     * @throws MQClientException MQ客户端异常
     */
    public void registerBroadcastConsumer(String topic, String tag, String consumerGroup, 
                                         MessageListenerConcurrently messageListener) throws MQClientException {
        registerConsumer(topic, tag, consumerGroup, messageListener, "BROADCASTING");
    }

    /**
     * 注册消费者
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param messageListener 消息监听器
     * @param messageModel 消息模式（CLUSTERING或BROADCASTING）
     * @throws MQClientException MQ客户端异常
     */
    private void registerConsumer(String topic, String tag, String consumerGroup, 
                                 MessageListenerConcurrently messageListener, String messageModel) throws MQClientException {
        String consumerKey = consumerGroup + ":" + topic + ":" + tag;
        if (consumers.containsKey(consumerKey)) {
            log.warn("Consumer already registered for key: {}", consumerKey);
            return;
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        // 消息模型设置暂时注释，避免依赖可能不存在的类
        // 后续可根据实际使用的 RocketMQ 版本调整
        consumer.setConsumeThreadMin(rocketMQProperties.getConsumer().getConsumeThreadMin());
        consumer.setConsumeThreadMax(rocketMQProperties.getConsumer().getConsumeThreadMax());
        consumer.setPullInterval(rocketMQProperties.getConsumer().getPullInterval());
        consumer.setPullBatchSize(rocketMQProperties.getConsumer().getPullBatchSize());

        // 注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, 
                                                           ConsumeConcurrentlyContext context) {
                try {
                    log.info("Consuming {} messages from topic: {}", msgs.size(), topic);
                    ConsumeConcurrentlyStatus status = messageListener.consumeMessage(msgs, context);
                    log.info("Message consumption status: {}", status);
                    return status;
                } catch (Exception e) {
                    log.error("Error consuming message: {}", e.getMessage(), e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        // 订阅主题和标签
        consumer.subscribe(topic, tag);
        log.info("Subscribed to topic: {} with tag: {} and message model: {}", topic, tag, messageModel);

        // 启动消费者
        consumer.start();
        log.info("Consumer started: {} for topic: {}", consumerGroup, topic);

        consumers.put(consumerKey, consumer);
    }

    /**
     * 注销消费者
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     */
    public void unregisterConsumer(String topic, String tag, String consumerGroup) {
        String consumerKey = consumerGroup + ":" + topic + ":" + tag;
        DefaultMQPushConsumer consumer = consumers.remove(consumerKey);
        if (consumer != null) {
            consumer.shutdown();
            log.info("Consumer shutdown: {} for topic: {}", consumerGroup, topic);
        } else {
            log.warn("No consumer found for key: {}", consumerKey);
        }
    }

    /**
     * 暂停消费
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     */
    public void suspendConsumer(String topic, String tag, String consumerGroup) {
        String consumerKey = consumerGroup + ":" + topic + ":" + tag;
        DefaultMQPushConsumer consumer = consumers.get(consumerKey);
        if (consumer != null) {
            consumer.suspend();
            log.info("Consumer suspended: {} for topic: {}", consumerGroup, topic);
        } else {
            log.warn("No consumer found for key: {}", consumerKey);
        }
    }

    /**
     * 恢复消费
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     */
    public void resumeConsumer(String topic, String tag, String consumerGroup) {
        String consumerKey = consumerGroup + ":" + topic + ":" + tag;
        DefaultMQPushConsumer consumer = consumers.get(consumerKey);
        if (consumer != null) {
            consumer.resume();
            log.info("Consumer resumed: {} for topic: {}", consumerGroup, topic);
        } else {
            log.warn("No consumer found for key: {}", consumerKey);
        }
    }

    /**
     * 获取消费者状态
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @return 消费者状态
     */
    public String getConsumerStatus(String topic, String tag, String consumerGroup) {
        String consumerKey = consumerGroup + ":" + topic + ":" + tag;
        DefaultMQPushConsumer consumer = consumers.get(consumerKey);
        if (consumer != null) {
            return consumer.getDefaultMQPushConsumerImpl().getServiceState().name();
        }
        return "NOT_REGISTERED";
    }

    /**
     * 关闭所有消费者
     */
    @PreDestroy
    public void shutdownAllConsumers() {
        log.info("Shutting down all consumers");
        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {
            try {
                entry.getValue().shutdown();
                log.info("Consumer shutdown: {}", entry.getKey());
            } catch (Exception e) {
                log.error("Error shutting down consumer {}: {}", entry.getKey(), e.getMessage(), e);
            }
        }
        consumers.clear();
    }

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        log.info("RocketMQConsumerService initialized");
    }
}
