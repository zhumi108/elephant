package com.tt.elephant.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailService {

    @Resource
    RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String mailAddress) throws MailSendException {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);

            SimpleMailMessage message = new SimpleMailMessage();
            //邮件发信人
            message.setFrom(from);
            //邮件收信人
            message.setTo(mailAddress);
            //邮件主题
            message.setSubject("新用户注册");
            //邮件内容
            // 生成随机 6位验证码
            int idenCode = (int) ((Math.random() * 9 + 1) * 100000);
            message.setText("验证码：" + idenCode);
            //发送邮件
            String key = "mail:" + mailAddress;
            mailSender.send(message);
            redisTemplate.opsForValue().set(key, idenCode, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new MailSendException("邮件发送失败:" + e.getMessage());
        }
    }
}
