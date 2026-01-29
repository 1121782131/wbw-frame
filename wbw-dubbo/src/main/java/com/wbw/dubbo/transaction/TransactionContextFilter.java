package com.wbw.dubbo.transaction;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -10000)
public class TransactionContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = invocation.getMethodName();
        String interfaceName = invoker.getInterface().getName();
        String fullMethodName = interfaceName + "." + methodName;

        // 处理消费者端的事务上下文传递
        if (RpcContext.getContext().isConsumerSide()) {
            String xid = TransactionContextHolder.getXid();
            if (xid != null) {
                // 将事务XID设置到Dubbo的RPC上下文中
                invocation.getAttachments().put("TX_XID", xid);
                TransactionLogger.logTransactionPropagate("Consumer", fullMethodName, xid);
            }
        }

        // 处理提供者端的事务上下文恢复
        if (RpcContext.getContext().isProviderSide()) {
            String xid = invocation.getAttachment("TX_XID");
            if (xid != null) {
                // 将事务XID绑定到当前线程
                TransactionContextHolder.setXid(xid);
                TransactionLogger.logTransactionPropagate("Provider", fullMethodName, xid);
            }
        }

        try {
            // 执行服务调用
            Result result = invoker.invoke(invocation);
            if (result.hasException()) {
                // 记录事务异常
                String xid = TransactionContextHolder.getXid();
                TransactionLogger.logTransactionError(fullMethodName, xid, result.getException().getMessage());
            }
            return result;
        } finally {
            // 清理事务上下文
            if (RpcContext.getContext().isProviderSide()) {
                TransactionContextHolder.clearXid();
            }
        }
    }
}
