package com.tt.elephant.controller;

import com.tt.elephant.jwt.JwtConstant;
import com.tt.elephant.jwt.JwtInterceptor;
import com.tt.elephant.jwt.JwtSupport;
import com.tt.elephant.jwt.JwtToken;
import com.tt.elephant.model.*;
import com.tt.elephant.repository.UserEntity;
import com.tt.elephant.repository.UserRepository;

import com.tt.elephant.util.ServiceSupport;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/api/v1")
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
        userEntity.setEmailAddress(registerDto.getEmailAddress());
        String originPassword = registerDto.getPassword();
        //1 判断是否已经注册过（从前注册但没有注销过）
        if (userRepository.findByEmailAddress(userEntity.getEmailAddress()) == 0) {
            userEntity.setStatus(1);
            userEntity.setCreateTime(System.currentTimeMillis());
            userEntity.setPassword(getMd5Hash(originPassword));
            userRepository.save(userEntity);
            return ResponseInfo.success("signup succeed");
        } else {
            return ResponseInfo.fail(500, "user already exists");
        }
    }

    public String getMd5Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(text.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported encoding exception");
        }
    }

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @PostMapping("/signin")
    public ResponseInfo login(@RequestBody LoginDto loginDto) {

        Integer emailAddressExists = userRepository.findByEmailAddress(loginDto.getEmailAddress());
        UserEntity result = userRepository.findByEmailAddressPassword(loginDto.getEmailAddress(), getMd5Hash(loginDto.getPassword()));

        if (result != null) {
            //jwt save token
            String token = JwtSupport.genereateToken(result.getUserId(), result.getEmailAddress());
            TokenModel tokenModel = new TokenModel(result.getUserId(), token);
            result.setToken(token);
            UserDto dto = new UserDto();
            dto.setUserId(result.getUserId());
            dto.setEmailAddress(result.getEmailAddress());
            String nickname = result.getNickname();
            String avartarUrl = result.getAvatarUrl();
            if (nickname == null) {
                dto.setNickname("");
            } else {
                dto.setNickname(nickname);
            }
            if (avartarUrl == null) {
                dto.setAvatarUrl("");
            } else {
                dto.setAvatarUrl(avartarUrl);
            }
            dto.setToken(result.getToken());
            dto.setStatus(result.getStatus());
            userRepository.save(result);
            return ResponseInfo.success("login succeed", dto);
        } else if (emailAddressExists == 0) {
            return ResponseInfo.fail(500, "user does not exist");
        }  else {
            return ResponseInfo.fail(500, "wrong password");
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
     * @param
     * @return
     */
    @JwtToken
    @PostMapping("/delete/account")
    public  ResponseInfo cancelAccount() {
        String userId = ServiceSupport.getCurrentUserId();
//        Integer result = userRepository.updateStatus(0, currentToken);
        userRepository.deleteById(userId);
        Optional<UserEntity> entity = userRepository.findById(userId);

        if (entity.isPresent()) {
            return ResponseInfo.fail(" delete account failed");
        } else {
            return ResponseInfo.success(" delete account success");
        }
    }

    /**
     *  判断当前用户是否已经注册过（排除已经注销过的用户）
     *  0  代表数据库中不存在；1带表存在
     * @param emailAddress
     * @return
     */
    @JwtToken
    @GetMapping("/findUser")
    public @ResponseBody ResponseInfo findUser(@RequestParam(value = "emailAddress") String emailAddress) {
        ResponseInfo responseInfo = new ResponseInfo();
        Integer result = userRepository.findByEmailAddress(emailAddress);
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

        String emailAddress =JwtSupport.getEmailAddress(token);

        return ResponseInfo.success(emailAddress);
    }

}