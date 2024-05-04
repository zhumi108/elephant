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

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return ResponseInfo.fail("failed",e);
    }

}
