package com.haiemdavang.AnrealShop.mail.service;

public interface IMailService {
    void sendOTP(String email);
    boolean verifyOTP(String otp, String email);
    void delOTP(String email);
    String getcode();
}
