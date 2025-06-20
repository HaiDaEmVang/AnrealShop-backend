package com.haiemdavang.AnrealShop.mail.service;

import com.haiemdavang.AnrealShop.mail.MailType;

public interface IMailService {
    void sendOTP(String email, String type);
    boolean verifyOTP(String otp, String email);
    void delOTP(String email);
    String getcode();
}
