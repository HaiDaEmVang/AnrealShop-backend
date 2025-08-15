package com.haiemdavang.AnrealShop.service.payment;

import com.haiemdavang.AnrealShop.dto.payment.PaymentRequestDto;
import com.haiemdavang.AnrealShop.tech.vnpay.VNPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService {

    private final VNPayConfig vnPayConfig;

    public String createPaymentUrl(PaymentRequestDto paymentRequestDTO, String ipAddress) {
        Map<String, String> vnpParams = vnPayConfig.getConfig();

        vnpParams.put("vnp_Amount", String.valueOf(paymentRequestDTO.getAmount() * 100));
        vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        vnpParams.put("vnp_TxnRef", paymentRequestDTO.getOrderId());
        vnpParams.put("vnp_OrderInfo", paymentRequestDTO.getOrderInfo());
        vnpParams.put("vnp_IpAddr", ipAddress);

        // Sắp xếp key
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        List<String> hashDataList = new ArrayList<>();
        List<String> queryList = new ArrayList<>();

        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedKey = URLEncoder.encode(fieldName, StandardCharsets.UTF_8);
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8);

                hashDataList.add(fieldName + "=" + encodedValue);
                queryList.add(encodedKey + "=" + encodedValue);
            }
        }

        String hashData = String.join("&", hashDataList);
        String query = String.join("&", queryList);

        String vnpSecureHash = hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        query += "&vnp_SecureHash=" + vnpSecureHash;

        return vnPayConfig.getVnp_PayUrl() + "?" + query;
    }


    public boolean validatePaymentResponse(Map<String, String> response) {
        String vnpSecureHash = response.get("vnp_SecureHash");

        Map<String, String> vnpParams = new HashMap<>(response);
        vnpParams.remove("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    hashData.append('&');
                }
            }
        }

        String calculatedHash = hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        return calculatedHash.equals(vnpSecureHash);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hmacData = hmac.doFinal(data.getBytes());
            return bytesToHex(hmacData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha512", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}