// com/wbw/common/exception/BusinessException.java
package com.wbw.common.exception;

import com.wbw.common.constant.BusinessConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 业务异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private Integer code;
    
    /**
     * 错误信息
     */
    private String message;
    
    /**
     * 错误详情
     */
    private Object data;
    
    /**
     * 构造函数
     */
    public BusinessException() {
        this(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, "系统异常");
    }
    
    public BusinessException(String message) {
        this(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, message);
    }
    
    public BusinessException(Integer code, String message) {
        this(code, message, null);
    }
    
    public BusinessException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 静态工厂方法
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }
    
    public static BusinessException of(Integer code, String message) {
        return new BusinessException(code, message);
    }
    
    public static BusinessException of(Integer code, String message, Object data) {
        return new BusinessException(code, message, data);
    }
    
    /**
     * 常见业务异常
     */
    public static BusinessException badRequest() {
        return new BusinessException(BusinessConstant.StatusCode.BAD_REQUEST, "请求参数错误");
    }
    
    public static BusinessException badRequest(String message) {
        return new BusinessException(BusinessConstant.StatusCode.BAD_REQUEST, message);
    }
    
    public static BusinessException unauthorized() {
        return new BusinessException(BusinessConstant.StatusCode.UNAUTHORIZED, "未授权");
    }
    
    public static BusinessException forbidden() {
        return new BusinessException(BusinessConstant.StatusCode.FORBIDDEN, "禁止访问");
    }
    
    public static BusinessException notFound() {
        return new BusinessException(BusinessConstant.StatusCode.NOT_FOUND, "资源不存在");
    }
    
    public static BusinessException conflict() {
        return new BusinessException(BusinessConstant.StatusCode.CONFLICT, "资源冲突");
    }
    
    public static BusinessException serverError() {
        return new BusinessException(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, "服务器内部错误");
    }
}