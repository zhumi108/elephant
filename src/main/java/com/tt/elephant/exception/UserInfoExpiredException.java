package com.tt.elephant.exception;

/**
 * token过期, 需要重新登录
 */
public class UserInfoExpiredException extends RuntimeException {

    public UserInfoExpiredException() {
        super();
    }

    public UserInfoExpiredException(String message) {
        super(message);
    }

    public UserInfoExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
