// com/wbw/common/exception/SystemException.java
package com.wbw.common.exception;

import com.wbw.common.constant.BusinessConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private Integer code;
    
    /**
     * 错误信息
     */
    private String message;
    
    public SystemException() {
        this(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, "系统异常");
    }
    
    public SystemException(String message) {
        this(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, message);
    }
    
    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}