package com.wbw.dubbo.filter;

import com.wbw.security.context.JwtSecurityContext;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JWT Dubbo上下文过滤器
 * 用于在Dubbo服务调用过程中传递JWT令牌
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -9999)
public class JwtDubboContextFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(JwtDubboContextFilter.class);
    private static final String JWT_TOKEN_KEY = "JWT_TOKEN";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = invocation.getMethodName();
        String interfaceName = invoker.getInterface().getName();
        String fullMethodName = interfaceName + "." + methodName;

        // 处理消费者端的JWT令牌传递
        if (RpcContext.getContext().isConsumerSide()) {
            String token = JwtSecurityContext.getToken();
            if (token != null) {
                // 将JWT令牌设置到Dubbo的RPC上下文中
                invocation.getAttachments().put(JWT_TOKEN_KEY, token);
                log.debug("消费者端传递JWT令牌: {} -> {}", fullMethodName, token.substring(0, 20) + "...");
            }
        }

        // 处理提供者端的JWT令牌恢复
        if (RpcContext.getContext().isProviderSide()) {
            String token = invocation.getAttachment(JWT_TOKEN_KEY);
            if (token != null) {
                // 将JWT令牌设置到安全上下文中
                JwtSecurityContext.setToken(token);
                log.debug("提供者端恢复JWT令牌: {} -> {}", fullMethodName, token.substring(0, 20) + "...");
            }
        }

        try {
            // 执行服务调用
            Result result = invoker.invoke(invocation);
            if (result.hasException()) {
                // 记录异常信息
                log.error("服务调用异常: {} - {}", fullMethodName, result.getException().getMessage());
            }
            return result;
        } finally {
            // 清理提供者端的JWT上下文
            if (RpcContext.getContext().isProviderSide()) {
                JwtSecurityContext.clear();
                log.debug("提供者端清理JWT上下文: {}", fullMethodName);
            }
        }
    }
}
