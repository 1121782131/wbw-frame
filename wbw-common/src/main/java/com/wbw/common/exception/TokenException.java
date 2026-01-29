package com.wbw.common.exception;

/**
 * Token异常
 */
public class TokenException extends SecurityException {

    public TokenException() {
        super("Token无效");
    }

    public TokenException(String message) {
        super(message);
    }
}
