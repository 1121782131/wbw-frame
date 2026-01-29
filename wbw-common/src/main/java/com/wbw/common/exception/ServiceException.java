// com/wbw/common/exception/ServiceException.java
package com.wbw.common.exception;

import com.wbw.common.constant.BusinessConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 服务异常类
 * 用于服务层的业务异常处理
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends BusinessException implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public ServiceException() {
        super(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, "服务异常");
    }

    public ServiceException(String message) {
        super(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, message);
    }

    public ServiceException(Integer code, String message) {
        super(code, message);
    }

    public ServiceException(Integer code, String message, Object data) {
        super(code, message, data);
    }

    /**
     * 静态工厂方法
     */
    public static ServiceException of(String message) {
        return new ServiceException(message);
    }

    public static ServiceException of(Integer code, String message) {
        return new ServiceException(code, message);
    }

    public static ServiceException of(Integer code, String message, Object data) {
        return new ServiceException(code, message, data);
    }

    /**
     * 常见服务异常
     */
    public static ServiceException serviceUnavailable() {
        return new ServiceException(BusinessConstant.StatusCode.SERVICE_UNAVAILABLE, "服务暂不可用");
    }

    public static ServiceException serviceUnavailable(String message) {
        return new ServiceException(BusinessConstant.StatusCode.SERVICE_UNAVAILABLE, message);
    }

    public static ServiceException timeout() {
        return new ServiceException(BusinessConstant.StatusCode.GATEWAY_TIMEOUT, "服务调用超时");
    }

    public static ServiceException timeout(String message) {
        return new ServiceException(BusinessConstant.StatusCode.GATEWAY_TIMEOUT, message);
    }

    public static ServiceException businessError() {
        return new ServiceException(BusinessConstant.StatusCode.BAD_REQUEST, "业务处理错误");
    }

    public static ServiceException businessError(String message) {
        return new ServiceException(BusinessConstant.StatusCode.BAD_REQUEST, message);
    }
}
