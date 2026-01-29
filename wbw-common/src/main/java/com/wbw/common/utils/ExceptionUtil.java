// com/wbw/common/exception/ExceptionUtil.java
package com.wbw.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 */
public class ExceptionUtil {
    
    private ExceptionUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * 获取异常堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return throwable.toString();
        }
    }
    
    /**
     * 获取根异常信息
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
    
    /**
     * 获取根异常信息
     */
    public static String getRootCauseMessage(Throwable throwable) {
        Throwable rootCause = getRootCause(throwable);
        return rootCause != null ? rootCause.getMessage() : null;
    }
    
    /**
     * 判断异常是否由指定异常引起
     */
    public static boolean isCausedBy(Throwable throwable, Class<? extends Throwable> causeClass) {
        if (throwable == null || causeClass == null) {
            return false;
        }
        
        Throwable current = throwable;
        while (current != null) {
            if (causeClass.isAssignableFrom(current.getClass())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}