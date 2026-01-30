package com.wbw.web.handler;

import com.wbw.common.constant.BusinessConstant;
import com.wbw.common.exception.*;
import com.wbw.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessException(BusinessException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[业务异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 服务异常处理
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleServiceException(ServiceException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[服务异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 权限异常处理
     */
    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handlePermissionException(PermissionException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[权限异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 安全异常处理
     */
    @ExceptionHandler(com.wbw.common.exception.SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleSecurityException(com.wbw.common.exception.SecurityException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[安全异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 系统异常处理
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSystemException(SystemException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[系统异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 令牌异常处理
     */
    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleTokenException(TokenException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[令牌异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 用户认证异常处理
     */
    @ExceptionHandler(UserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleUserAuthenticationException(UserAuthenticationException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[用户认证异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 验证异常处理
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(ValidationException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[验证异常-{}] 代码: {}, 消息: {}", errorId, e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 参数校验异常 - @Validated @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[参数校验异常-{}] 消息: {}", errorId, e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Result.error(BusinessConstant.StatusCode.BAD_REQUEST,"参数校验失败", errors);
    }
    
    /**
     * 参数绑定异常 - @ModelAttribute
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleBindException(BindException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[参数绑定异常-{}] 消息: {}", errorId, e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Result.error(BusinessConstant.StatusCode.BAD_REQUEST,"参数绑定失败", errors);
    }
    
    /**
     * ConstraintViolationException - @Validated在方法参数上使用
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleConstraintViolationException(
            ConstraintViolationException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[约束违反异常-{}] 消息: {}", errorId, e.getMessage());
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }
        return Result.error(BusinessConstant.StatusCode.BAD_REQUEST,"参数校验失败", errors);
    }
    
    /**
     * 404异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e,
                                                      HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("[404异常-{}] 请求路径不存在: {} {}", errorId, request.getMethod(), request.getRequestURI());
        return Result.error(HttpStatus.NOT_FOUND.value(), "请求路径不存在");
    }
    
    /**
     * 请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("[请求方法不支持异常-{}] 请求方法不支持: {} {}", errorId, request.getMethod(), request.getRequestURI());
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方法不支持");
    }
    
    /**
     * 其他未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("[系统异常-{}] {} {} - {}", errorId, request.getMethod(), request.getRequestURI(), 
                 e.getMessage(), e);
        return Result.error(BusinessConstant.StatusCode.INTERNAL_SERVER_ERROR, "系统异常，请稍后再试");
    }
}