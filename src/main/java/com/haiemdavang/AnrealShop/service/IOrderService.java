package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.checkout.CheckoutRequestDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutResponseDto;
import com.haiemdavang.AnrealShop.dto.payment.PaymentResponseDto;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;

public interface IOrderService {
    CheckoutResponseDto createOrderBankTran(CheckoutRequestDto requestDto, UserAddress userAddress, String ipAddress);

    CheckoutResponseDto createOrderCOD(CheckoutRequestDto requestDto, UserAddress userAddress);

    void handleSuccessfulPayment(String orderId);

    void handleFailedPayment(String orderId, String responseCode);

    PaymentResponseDto getPaymentResult(String orderId);
}
