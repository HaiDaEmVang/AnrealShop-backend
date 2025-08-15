package com.haiemdavang.AnrealShop.tech.vnpay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class VNPayConfig {

    @Getter
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;

    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;

    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode ;

    @Getter
    @Value("${payment.vnPay.secretKey}")
    private String secretKey;

    @Value("${payment.vnPay.version}")
    private String vnp_Version;

    @Value("${payment.vnPay.command}")
    private String vnp_Command;

    @Value("${payment.vnPay.orderType}")
    private String orderType;

    @Value("${payment.vnPay.timeExpire}")
    private int timeExpire;

    public Map<String, String> getConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        vnpParamsMap.put("vnp_ExpireDate", new SimpleDateFormat("yyyyMMddHHmmss").format(
                new Date(System.currentTimeMillis() + (long) this.timeExpire * 60 * 1000)));
        return vnpParamsMap;
    }

}