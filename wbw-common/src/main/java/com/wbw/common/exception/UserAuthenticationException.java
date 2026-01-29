package com.wbw.common.exception;

/**
 * 用户认证异常
 */
public class UserAuthenticationException extends SecurityException {

    public UserAuthenticationException() {
        super("用户认证失败");
    }

    public UserAuthenticationException(String message) {
        super(message);
    }
}
