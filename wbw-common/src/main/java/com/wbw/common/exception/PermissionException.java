package com.wbw.common.exception;

/**
 * 权限异常
 */
public class PermissionException extends SecurityException {

    public PermissionException() {
        super(403, "权限不足");
    }

    public PermissionException(String message) {
        super(403, message);
    }
}
