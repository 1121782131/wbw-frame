package com.wbw.rocketmq.exception;

import lombok.Getter;

/**
 * RocketMQ异常类
 */
@Getter
public class RocketMQException extends RuntimeException {

    private final String errorCode;

    public RocketMQException(String message) {
        super(message);
        this.errorCode = "ROCKETMQ_ERROR";
    }

    public RocketMQException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RocketMQException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ROCKETMQ_ERROR";
    }

    public RocketMQException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
