package com.tt.elephant.jwt;


import com.tt.elephant.exception.CustomException;
import com.tt.elephant.repository.UserEntity;
import com.tt.elephant.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;


public class JwtInterceptor implements HandlerInterceptor {

@Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader(JwtConstant.TOKEN);
        // 如果不是映射到方法直接通过
        if(!(o instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)o;
        Method method=handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(JwtToken.class)) {
            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
            if (jwtToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new CustomException("无token，请重新登录");
                }
                // 验证 token
              if( ! JwtSupport.verifyToken(token)){
                  throw  new CustomException("token不合法");
              }
              //获取过期时间
            Date date =   JwtSupport.getExpireTime(token);
              System.out.println("date:"+date);

                //验证用户
              if(!checkUser(token)){
                  throw  new CustomException("当前用户不存在，非法");
              }

            }
        }
        return true;
    }

    /**
     * 验证是否为同一用户
     * @param token
     * @return
     */
    private boolean checkUser(String token) {
        //1从token 获取用户  userId, username;
        String userId = JwtSupport.getUserId(token);
        String emailAddress = JwtSupport.getEmailAddress(token);
        //2   判断当前用户是否为相同，从数据库中获取username,根据当前userid
        UserEntity userEntity = userService.getUser(userId);
        if (userEntity != null) {
                    if (Objects.equals(userEntity.getEmailAddress(), emailAddress)){
                        return true;}
        }
        return false;

    }

}

