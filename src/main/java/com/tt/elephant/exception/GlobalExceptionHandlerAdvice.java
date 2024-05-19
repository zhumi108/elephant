package com.tt.elephant.exception;

import com.tt.elephant.model.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public Object handleRuntimeException(Exception e, HttpServletRequest request, HttpServletResponse response) {

        if (e.getMessage() == "登录过期, 请重新登录") {
            response.setStatus(10011);
            return ResponseInfo.fail(10011,e);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ResponseInfo.fail(500,e);
        }
    }
}
