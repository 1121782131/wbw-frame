// com/wbw/common/result/Result.java
package com.wbw.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wbw.common.constant.BusinessConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果类
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 提示信息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 跟踪ID
     */
    private String traceId;
    
    /**
     * 成功标志
     */
    private Boolean success;
    
    /**
     * 构造函数
     */
    Result(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(BusinessConstant.StatusCode.SUCCESS, message, data, true);
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> error() {
        return error("操作失败");
    }
    
    public static <T> Result<T> error(String message) {
        return error(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, message);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return error(code, message, null);
    }
    
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<>(code, message, data, false);
    }
    
    /**
     * 根据布尔值返回结果
     */
    public static <T> Result<T> judge(boolean flag) {
        return flag ? success() : error();
    }
    
    public static <T> Result<T> judge(boolean flag, String successMsg, String errorMsg) {
        return flag ? success(successMsg, null) : error(errorMsg);
    }
    
    /**
     * 设置跟踪ID
     */
    public Result<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(success);
    }
}