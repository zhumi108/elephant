package com.tt.elephant.util;

import com.tt.elephant.jwt.JwtConstant;
import com.tt.elephant.jwt.JwtSupport;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 获取token方式：有三种，
 * controller有@RequestToken注解，service有@RequestToken注解，controller和service都有@RequestToken注解
 * service 获取token
 */
public class ServiceSupport {
    public static String getCurrentToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        return request.getHeader(JwtConstant.TOKEN);
    }

    public static String getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        return request.getHeader(JwtConstant.CLAIM_KEY_USER_ID);
    }
}
