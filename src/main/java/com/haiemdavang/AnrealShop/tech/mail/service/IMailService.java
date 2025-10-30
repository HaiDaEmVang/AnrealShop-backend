package com.haiemdavang.AnrealShop.tech.mail.service;

public interface IMailService {
    void sendOTP(String email, String type);
    boolean verifyOTP(String otp, String email);
    void delOTP(String email);
    String getCode();
}
