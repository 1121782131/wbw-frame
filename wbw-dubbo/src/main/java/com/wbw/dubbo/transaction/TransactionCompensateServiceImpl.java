package com.wbw.dubbo.transaction;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事务补偿服务实现类
 */
@Service
public class TransactionCompensateServiceImpl implements TransactionCompensateService {

    // 模拟补偿日志存储
    private static final Map<String, CompensateLog> COMPENSATE_LOGS = new ConcurrentHashMap<>();
    // 模拟补偿重试计数
    private static final Map<String, AtomicInteger> RETRY_COUNTS = new ConcurrentHashMap<>();

    @Override
    public boolean compensate(String transactionId, String compensateData) {
        try {
            System.out.println("执行事务补偿：" + transactionId + "，补偿数据：" + compensateData);
            
            // 这里实现具体的补偿逻辑
            // 例如：回滚已执行的操作、恢复数据状态等
            
            // 记录补偿日志
            recordCompensateLog(transactionId, compensateData, "SUCCESS", "补偿执行成功");
            
            return true;
        } catch (Exception e) {
            System.err.println("事务补偿失败：" + e.getMessage());
            recordCompensateLog(transactionId, compensateData, "FAILED", "补偿执行失败：" + e.getMessage());
            return false;
        }
    }

    @Override
    public void recordCompensateLog(String transactionId, String compensateData, String status, String message) {
        CompensateLog log = new CompensateLog();
        log.setTransactionId(transactionId);
        log.setCompensateData(compensateData);
        log.setStatus(status);
        log.setMessage(message);
        log.setTimestamp(System.currentTimeMillis());
        COMPENSATE_LOGS.put(transactionId, log);
        System.out.println("记录补偿日志：" + transactionId + "，状态：" + status + "，消息：" + message);
    }

    @Override
    public boolean retryCompensate(String transactionId, int maxRetries) {
        AtomicInteger retryCount = RETRY_COUNTS.computeIfAbsent(transactionId, k -> new AtomicInteger(0));
        
        if (retryCount.get() >= maxRetries) {
            System.out.println("事务补偿重试次数达到上限：" + transactionId);
            return false;
        }
        
        int currentRetry = retryCount.incrementAndGet();
        System.out.println("执行事务补偿重试：" + transactionId + "，第" + currentRetry + "次重试");
        
        // 获取补偿数据
        CompensateLog log = COMPENSATE_LOGS.get(transactionId);
        if (log == null) {
            System.err.println("未找到事务补偿日志：" + transactionId);
            return false;
        }
        
        // 执行补偿
        return compensate(transactionId, log.getCompensateData());
    }

    /**
     * 补偿日志内部类
     */
    private static class CompensateLog {
        private String transactionId;
        private String compensateData;
        private String status;
        private String message;
        private long timestamp;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCompensateData() {
            return compensateData;
        }

        public void setCompensateData(String compensateData) {
            this.compensateData = compensateData;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
