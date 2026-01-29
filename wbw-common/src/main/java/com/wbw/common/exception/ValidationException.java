// com/wbw/common/exception/ValidationException.java
package com.wbw.common.exception;

import com.wbw.common.constant.BusinessConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 参数验证异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误字段映射
     */
    private Map<String, String> fieldErrors;
    
    /**
     * 错误列表
     */
    private List<String> errorList;
    
    public ValidationException(String message) {
        super(BusinessConstant.StatusCode.BAD_REQUEST, message);
    }
    
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(BusinessConstant.StatusCode.BAD_REQUEST, message);
        this.fieldErrors = fieldErrors;
    }
    
    public ValidationException(String message, List<String> errorList) {
        super(BusinessConstant.StatusCode.BAD_REQUEST, message);
        this.errorList = errorList;
    }
}