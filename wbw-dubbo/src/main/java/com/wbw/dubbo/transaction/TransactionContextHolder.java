package com.wbw.dubbo.transaction;

import io.seata.core.context.RootContext;

public class TransactionContextHolder {

    private static final ThreadLocal<String> XID_HOLDER = new ThreadLocal<>();

    /**
     * 获取当前事务的XID
     * @return 事务XID
     */
    public static String getXid() {
        return RootContext.getXID();
    }

    /**
     * 设置事务XID到当前线程
     * @param xid 事务XID
     */
    public static void setXid(String xid) {
        if (xid != null) {
            RootContext.bind(xid);
            XID_HOLDER.set(xid);
        }
    }

    /**
     * 清除当前线程的事务XID
     */
    public static void clearXid() {
        RootContext.unbind();
        XID_HOLDER.remove();
    }

    /**
     * 检查当前是否存在事务上下文
     * @return 是否存在事务上下文
     */
    public static boolean hasTransactionContext() {
        return getXid() != null;
    }
}
