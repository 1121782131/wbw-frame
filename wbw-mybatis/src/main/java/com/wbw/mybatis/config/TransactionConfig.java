package com.wbw.mybatis.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;

/**
 * 事务配置类
 * 为后续项目提供自动事务支持
 * 项目只需添加 @Transactional 注解即可启用事务
 */
@Configuration
@EnableTransactionManagement(order = Ordered.LOWEST_PRECEDENCE - 1)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionConfig {
    
    /**
     * 数据源配置
     * 如果项目没有自定义数据源，这里会创建一个默认的
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        // 这里设置一些默认的数据库连接参数
        // 实际项目应该通过配置文件设置
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    /**
     * 事务管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 事务拦截器（提供默认的事务行为）
     */
    @Bean
    @ConditionalOnMissingBean(TransactionInterceptor.class)
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(transactionManager);
        
        // 创建事务属性源
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        
        // 配置方法匹配规则和事务属性
        org.springframework.transaction.interceptor.RuleBasedTransactionAttribute defaultAttribute = 
            new org.springframework.transaction.interceptor.RuleBasedTransactionAttribute();
        
        // 默认事务属性：REQUIRED传播级别，所有异常都回滚
        defaultAttribute.setPropagationBehavior(org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED);
        defaultAttribute.setRollbackRules(java.util.Collections.singletonList(
            new org.springframework.transaction.interceptor.RollbackRuleAttribute(Throwable.class)
        ));
        
        // 只读事务属性
        org.springframework.transaction.interceptor.RuleBasedTransactionAttribute readOnlyAttribute = 
            new org.springframework.transaction.interceptor.RuleBasedTransactionAttribute();
        readOnlyAttribute.setPropagationBehavior(org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED);
        readOnlyAttribute.setReadOnly(true);
        
        // 查询方法：只读事务
        source.addTransactionalMethod("get*", readOnlyAttribute);
        source.addTransactionalMethod("find*", readOnlyAttribute);
        source.addTransactionalMethod("select*", readOnlyAttribute);
        source.addTransactionalMethod("query*", readOnlyAttribute);
        source.addTransactionalMethod("count*", readOnlyAttribute);
        source.addTransactionalMethod("list*", readOnlyAttribute);
        source.addTransactionalMethod("search*", readOnlyAttribute);
        
        // 写方法：需要事务，所有异常都回滚
        source.addTransactionalMethod("save*", defaultAttribute);
        source.addTransactionalMethod("insert*", defaultAttribute);
        source.addTransactionalMethod("update*", defaultAttribute);
        source.addTransactionalMethod("delete*", defaultAttribute);
        source.addTransactionalMethod("remove*", defaultAttribute);
        source.addTransactionalMethod("batch*", defaultAttribute);
        source.addTransactionalMethod("create*", defaultAttribute);
        source.addTransactionalMethod("modify*", defaultAttribute);
        
        // 默认匹配所有方法
        source.addTransactionalMethod("*", defaultAttribute);
        
        interceptor.setTransactionAttributeSource(source);
        
        return interceptor;
    }

    /**
     * 事务切面顾问（AOP配置）
     * 通过AOP为Service层自动添加事务
     */
    @Bean
    @ConditionalOnBean(TransactionInterceptor.class)
    @ConditionalOnProperty(name = "wbw.transaction.global-interceptor", havingValue = "true", matchIfMissing = true)
    public Advisor transactionAdvisor(TransactionInterceptor transactionInterceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        
        // 切点表达式：匹配所有Service层的方法
        pointcut.setExpression(
            "execution(* com.wbw..*.service..*.*(..)) || " +
            "execution(* com.wbw..*.service..*.*(..))"
        );
        
        return new DefaultPointcutAdvisor(pointcut, transactionInterceptor);
    }
}