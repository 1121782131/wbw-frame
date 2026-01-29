package com.wbw.mybatis.util;

import lombok.experimental.UtilityClass;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 事务工具类
 * 提供编程式事务管理
 */
@UtilityClass
public class TransactionUtil {
    
    /**
     * 执行无返回值的事务操作
     * @param transactionManager 事务管理器
     * @param action 要执行的操作
     */
    public static void execute(PlatformTransactionManager transactionManager, 
                              Consumer<TransactionStatus> action) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            action.accept(status);
            transactionManager.commit(status);
        } catch (Throwable e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
    
    /**
     * 执行有返回值的事务操作
     * @param transactionManager 事务管理器
     * @param function 要执行的操作
     * @param <T> 返回值类型
     * @return 执行结果
     */
    public static <T> T execute(PlatformTransactionManager transactionManager, 
                               Function<TransactionStatus, T> function) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            T result = function.apply(status);
            transactionManager.commit(status);
            return result;
        } catch (Throwable e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
    
    /**
     * 创建事务模板
     * @param transactionManager 事务管理器
     * @return TransactionTemplate
     */
    public static TransactionTemplate createTransactionTemplate(
            PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }
    
    /**
     * 使用事务模板执行操作
     * @param transactionManager 事务管理器
     * @param callback 事务回调
     * @param <T> 返回值类型
     * @return 执行结果
     */
    public static <T> T executeWithTemplate(
            PlatformTransactionManager transactionManager, 
            TransactionCallback<T> callback) {
        TransactionTemplate template = createTransactionTemplate(transactionManager);
        return template.execute(callback);
    }
    
    /**
     * 执行只读事务
     * @param transactionManager 事务管理器
     * @param action 要执行的操作
     */
    public static void executeReadOnly(PlatformTransactionManager transactionManager, 
                                      Runnable action) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        definition.setReadOnly(true);
        
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            action.run();
            transactionManager.commit(status);
        } catch (Throwable e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
    
    /**
     * 检查当前是否在事务中
     * @param transactionManager 事务管理器
     * @return 是否在事务中
     */
    public static boolean isInTransaction(PlatformTransactionManager transactionManager) {
        try {
            // 使用TransactionTemplate检查
            TransactionTemplate template = new TransactionTemplate(transactionManager);
            template.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
            
            return template.execute(status -> {
                // 如果当前在事务中，这里会抛出异常
                return false;
            });
        } catch (Exception e) {
            // 如果抛出异常，说明当前在事务中
            return true;
        }
    }
}