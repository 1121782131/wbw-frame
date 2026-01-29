// com/wbw/security/exception/SecurityException.java
package com.wbw.common.exception;

/**
 * 安全异常
 */
public class SecurityException extends BusinessException {
    
    public SecurityException() {
        super(401, "认证失败");
    }
    
    public SecurityException(String message) {
        super(401, message);
    }
    
    public SecurityException(Integer code, String message) {
        super(code, message);
    }
}



