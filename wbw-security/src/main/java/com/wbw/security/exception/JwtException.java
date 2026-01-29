package com.wbw.security.exception;

/**
 * JWT异常
 */
public class JwtException extends RuntimeException {
    
    private final int code;
    
    public JwtException(String message) {
        this(401, message);
    }
    
    public JwtException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public JwtException(String message, Throwable cause) {
        this(401, message, cause);
    }
    
    public JwtException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}