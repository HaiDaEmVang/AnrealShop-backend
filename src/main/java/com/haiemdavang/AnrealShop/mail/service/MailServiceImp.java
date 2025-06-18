package com.haiemdavang.AnrealShop.mail.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.haiemdavang.AnrealShop.mail.MailTemplate;
import com.haiemdavang.AnrealShop.mail.MailType;
import com.haiemdavang.AnrealShop.redis.service.IRedisService;
import com.haiemdavang.AnrealShop.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImp implements IMailService{
    private final IUserService userService;
    private final JavaMailSender javaMailSender;
    private final IRedisService redisService;
    @Value("${spring.mail.username}")
    private String mailFrom;

    private final String OTP_PREFIX = "otp_request:";


    @Override
    public void sendOTP(String email, MailType mailType) {
        if(userService.isExitsts(email))
            throw new AnrealShopException("USER_NOT_FOUND");
        int stamp = 0;

        if(redisService.isExists(OTP_PREFIX + email)) {
            stamp = Integer.parseInt(redisService.getValue(OTP_PREFIX + email));
        }
        int MAX_ATTEMPTS = 5;
        if(stamp >= MAX_ATTEMPTS) {
            redisService.addValue(OTP_PREFIX + email, stamp + "", 12, TimeUnit.HOURS);
            throw new AnrealShopException("OTP_DENIED");
        }else {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");
                mailHelper.setFrom(mailFrom);
                mailHelper.setSubject("Your OTP");
                String code = getcode();
                mailHelper.setText(MailTemplate.getEmailHTML(code, email, mailType), true);
                mailHelper.setTo(email);
                javaMailSender.send(mail);
                int EXPIRATION_TIME = 1;
                redisService.addValue(email, code, EXPIRATION_TIME, TimeUnit.MINUTES);
                redisService.addValue(OTP_PREFIX + email, stamp + 1 + "", 2, TimeUnit.HOURS);
            } catch (MessagingException e) {
                throw new AnrealShopException("EMAIL_EXCEPTION");
            }
        }
    }

    @Override
    public boolean verifyOTP(String otp, String email) {
        if(!redisService.isExists(email))
            throw new AnrealShopException("OTP_NOT_FOUND");
        if(redisService.getValue(email) == null || !redisService.getValue(email).equals(otp) )
            throw new AnrealShopException("OTP_INVALID");
        return true;
    }

    @Override
    public String getcode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    @Override
    public void delOTP(String email) {
        redisService.del(OTP_PREFIX + email);
        redisService.del(email);
    }




}
