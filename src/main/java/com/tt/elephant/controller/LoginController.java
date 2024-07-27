package com.tt.elephant.controller;

import com.tt.elephant.jwt.JwtConstant;
import com.tt.elephant.jwt.JwtInterceptor;
import com.tt.elephant.jwt.JwtSupport;
import com.tt.elephant.jwt.JwtToken;
import com.tt.elephant.model.*;
import com.tt.elephant.repository.UserEntity;
import com.tt.elephant.repository.UserRepository;

import com.tt.elephant.service.MailService;
import com.tt.elephant.util.ServiceSupport;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/api/v1")
public class LoginController {


    @Autowired(required = false)
    private UserRepository userRepository;

    @Autowired(required = false)
    private MailService mailService;

    /**
     * 用户注册校验
     * @param userInfo
     * @return
     */
    @PostMapping("/signup/check")
    public ResponseInfo signUpCheck(@RequestBody Map userInfo) {
        String emailAddress = (String) userInfo.get("emailAddress");
        //1 判断是否已经注册过（从前注册但没有注销过）
        if (userRepository.emailAddressExists(emailAddress) == 0) {
            return sendEmailCaptcha(userInfo);
        } else {
            return ResponseInfo.fail(500, "user already exists");
        }
    }

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
        if (userRepository.emailAddressExists(userEntity.getEmailAddress()) == 0) {
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

        Integer emailAddressExists = userRepository.emailAddressExists(loginDto.getEmailAddress());
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
            dto.setBlockedUsers(result.getBlockedUsers());
            userRepository.save(result);
            return ResponseInfo.success("login succeed", dto);
        } else if (emailAddressExists == 0) {
            return ResponseInfo.fail(500, "user does not exist");
        }  else {
            return ResponseInfo.fail(500, "wrong password");
        }
    }

    /**
    更新昵称和头像
    */
    @JwtToken
    @PostMapping("/fillUserInfo")
    public ResponseInfo fillUserInfo(@RequestBody Map userInfo) {
        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> result = userRepository.findById(userId);
        String nickname = (String) userInfo.get("nickname");
        String avartarUrl = (String) userInfo.get("avartarUrl");
        Integer nicknameExists = userRepository.nicknameExists(nickname);

        if (nicknameExists == 1) {
            return ResponseInfo.fail(500, "name has been taken");
        } else if (result != null) {
            UserEntity entity = result.get();
            entity.setNickname(nickname);
            entity.setAvatarUrl(avartarUrl);
            userRepository.save(entity);
            return ResponseInfo.success("user info updated");
        } else {
            return ResponseInfo.fail(500, "user does not exist");
        }
    }

    /**
     更新昵称
     */
    @JwtToken
    @PostMapping("/editNickname")
    public ResponseInfo editNickname(@RequestBody Map userInfo) {
        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> result = userRepository.findById(userId);
        String nickname = (String) userInfo.get("nickname");
        Integer nicknameExists = userRepository.nicknameExists(nickname);

        if (nicknameExists == 1) {
            return ResponseInfo.fail(500, "name has been taken");
        } else if (result != null) {
            UserEntity entity = result.get();
            entity.setNickname(nickname);
            userRepository.save(entity);
            return ResponseInfo.success("user info updated");
        } else {
            return ResponseInfo.fail(500, "user does not exist");
        }
    }

    @JwtToken
    @PostMapping("/block/user")
    public ResponseInfo blockUser(@RequestBody Map userInfo) {
        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> result = userRepository.findById(userId);
        UserEntity entity = result.get();
        String blockedUsers = entity.getBlockedUsers();
        String blockID = (String) userInfo.get("userId");
        if (blockID == null) {
            return ResponseInfo.fail(500, "parameter error");
        }

        if (blockedUsers == null || blockedUsers.isBlank()) {
            blockedUsers = blockID;
        } else {
            blockedUsers = blockedUsers + ',' + blockID;
        }
        entity.setBlockedUsers(blockedUsers);
        userRepository.save(entity);
        return ResponseInfo.success("You will never see this user again");
    }

    @JwtToken
    @PostMapping("/report/inappropriate")
    public ResponseInfo reportInappropriate(@RequestBody Map userInfo) {
        String reportedUserId = (String) userInfo.get("userId");
        String articleId = (String) userInfo.get("articleId");
        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> currenUser = userRepository.findById(userId);
        UserEntity currentEntity = currenUser.get();
        ResponseInfo responseInfo = new ResponseInfo();
        mailService.sendReportMail(currentEntity.getEmailAddress(), articleId);
        responseInfo.setCode(200);
        responseInfo.setMsg("Thanks for your report, we will review it within 24 hours.");
        return responseInfo;
    }

    @PostMapping("/resetPassword")
    public ResponseInfo resetPassword(@RequestBody Map userInfo) {
        String emailAddress = (String) userInfo.get("emailAddress");
        int result = userRepository.emailAddressExists(emailAddress);
        String newPassword = (String) userInfo.get("newPassword");

        if (result == 1) {
            UserEntity entity = userRepository.findByEmailAddress(emailAddress);
            entity.setPassword(getMd5Hash(newPassword));
            userRepository.save(entity);
            return ResponseInfo.success("password update succeed");
        } else {
            return ResponseInfo.fail(500, "user does not exist");
        }
    }

    @PostMapping("/sendEmailCaptcha")
    public ResponseInfo sendEmailCaptcha(@RequestBody Map userInfo) {
        String emailAddress = (String) userInfo.get("emailAddress");
        ResponseInfo responseInfo = new ResponseInfo();
        mailService.sendMail(emailAddress);
        responseInfo.setData(String.valueOf(mailService.captchaCode));
        responseInfo.setCode(200);
        responseInfo.setMsg("email send succeed");
        return responseInfo;
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

    @JwtToken
    @PostMapping("/changePassword")
    public ResponseInfo changePassword(@RequestBody Map params) {
        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> result = userRepository.findById(userId);
        String originPassword = (String) params.get("originPassword");
        String newPassword = (String) params.get("newPassword");

        if (result != null) {
            UserEntity entity = result.get();
            String savedPassword = entity.getPassword();
            if (getMd5Hash(originPassword).equals(savedPassword) == false) {
                return ResponseInfo.fail(500, "wrong original password");
            } else {
                entity.setPassword(getMd5Hash(newPassword));
                userRepository.save(entity);
                return ResponseInfo.success("password updated");
            }
        } else {
            return ResponseInfo.fail(500, "user does not exist");
        }
    }

    /**
     * 注销用户，将用户的状态改为0
     * @param
     * @return
     */
    @JwtToken
    @PostMapping("/delete/account")
    public  ResponseInfo deleteAccount() {
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
        Integer result = userRepository.emailAddressExists(emailAddress);
        return ResponseInfo.success(result);

    }

    /**
     * 获取用户信息
     *
     * @param
     * @return
     */
    @JwtToken
    @PostMapping("/getUserInfo")
    public @ResponseBody ResponseInfo getUserInfo() {
        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> result = userRepository.findById(userId);

        if (result != null) {
            UserEntity entity = result.get();
            UserDto dto = new UserDto();
            dto.setUserId(entity.getUserId());
            dto.setEmailAddress(entity.getEmailAddress());
            String nickname = entity.getNickname();
            String avartarUrl = entity.getAvatarUrl();
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
            dto.setToken(entity.getToken());
            dto.setStatus(entity.getStatus());
            dto.setBlockedUsers(entity.getBlockedUsers());
            return ResponseInfo.success("login succeed", dto);
        } else {
            return ResponseInfo.fail(500, "user does not exist");
        }
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