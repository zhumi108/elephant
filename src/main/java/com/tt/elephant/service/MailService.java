package com.tt.elephant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public int captchaCode;

    public void sendMail(String mailAddress) throws MailSendException {
        try {

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
            message.setText("【engo】您的校验码是 " + idenCode + ", 请妥善保管。如非本人操作, 请忽略!");
            //发送邮件
            mailSender.send(message);
            captchaCode = idenCode;
        } catch (Exception e) {
            throw new MailSendException("邮件发送失败:" + e.getMessage());
        }
    }
}
