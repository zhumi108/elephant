package com.tt.elephant.controller;

import com.tt.elephant.jwt.JwtSupport;
import com.tt.elephant.jwt.JwtToken;
import com.tt.elephant.model.LoginDto;
import com.tt.elephant.model.RegisterDto;
import com.tt.elephant.model.ResponseInfo;
import com.tt.elephant.model.TokenModel;
import com.tt.elephant.repository.UserEntity;
import com.tt.elephant.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@RestController
public class LoginController {


    @Autowired
    private UserRepository userRepository;


    /**
     * 用户注册
     * @param registerDto
     * @return
     */
    @PostMapping("/signup")
    public ResponseInfo signUp(@RequestBody RegisterDto registerDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(registerDto.getPassword());
        //1 判断是否已经注册过（从前注册但没有注销过）
        if (userRepository.findByUsername(userEntity.getUsername()) == 0) {
            userEntity.setStatus(1);
            userRepository.save(userEntity);
         return   ResponseInfo.success("sign up success");
        } else {
       return ResponseInfo.fail("signup failed");
        }
    }

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @PostMapping("/signin")
    public  ResponseInfo login(@RequestBody LoginDto loginDto) {

        UserEntity result = userRepository.findByUsernameAndpassword(loginDto.getUsername(), loginDto.getPassword());

        ResponseInfo responseInfo = new ResponseInfo();
        if (result != null) {
            //jwt save token
            String token = JwtSupport.genereateToken(result.getUserId(), result.getUsername());
            TokenModel tokenModel = new TokenModel(result.getUserId(),token);

            return ResponseInfo.success("login success",tokenModel);

        } else {
            return ResponseInfo.fail("login failed");

        }

    }

    /**
     * 用户退出
     * @param userId
     * @return
     */
    @JwtToken
    @GetMapping("/logout")
    public @ResponseBody ResponseInfo logout(@RequestParam(value = "userId") String userId) {
        ResponseInfo responseInfo = new ResponseInfo();
        return responseInfo;
    }
    /**
     * 注销用户，将用户的状态改为0
     * @param userId
     * @return
     */
    @JwtToken
    @GetMapping("/delete/account")
    public  ResponseInfo cancelAccount(@RequestParam(value = "userId") String userId) {
        ResponseInfo responseInfo = new ResponseInfo();
        Integer result = userRepository.updateStatus(0, userId);

        if (result != 0) {
          return ResponseInfo.success(" cancel account success");
        } else {
          return ResponseInfo.fail(" cancel account failed");
        }
    }

    /**
     *  判断当前用户是否已经注册过（排除已经注销过的用户）
     *  0  代表数据库中不存在；1带表存在
     * @param username
     * @return
     */
    @JwtToken
    @GetMapping("/findUser")
    public @ResponseBody ResponseInfo findUser(@RequestParam(value = "username") String username) {
        ResponseInfo responseInfo = new ResponseInfo();
        Integer result = userRepository.findByUsername(username);
        return ResponseInfo.success(result);

    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @JwtToken
    @GetMapping("/getUserInfo")
    public @ResponseBody ResponseInfo findUserByUserId(@RequestParam(value = "userId") String userId) {
        ResponseInfo responseInfo = new ResponseInfo();
        Optional<UserEntity> result = userRepository.findById(userId);
        return result.map(ResponseInfo::success).orElseGet(() -> ResponseInfo.fail("the current user does not exist!"));
    }


    @JwtToken
    @PostMapping(("/test"))
    public ResponseInfo test() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = httpServletRequest.getHeader("token");

        String username =JwtSupport.getUserName(token);

        return ResponseInfo.success(username);
    }

}