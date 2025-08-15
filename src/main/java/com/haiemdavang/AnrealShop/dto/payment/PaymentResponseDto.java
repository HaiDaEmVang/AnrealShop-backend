package com.haiemdavang.AnrealShop.dto.payment;

import com.haiemdavang.AnrealShop.modal.enums.PaymentGateway;
import com.haiemdavang.AnrealShop.modal.enums.PaymentStatus;
import com.haiemdavang.AnrealShop.modal.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private String orderId;
    private LocalDateTime orderDate;
    private LocalDateTime orderDateExpiration;
    private Long amount;
    private PaymentStatus paymentStatus;
    private PaymentType paymentMethod;
    private PaymentGateway paymentGateway;

    private boolean isTransfer;
}
