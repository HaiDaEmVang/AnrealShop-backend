package com.haiemdavang.AnrealShop.dto.checkout;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutResponseDto {
    private String orderId;
    private String message;
    private String urlRedirect;
    private boolean isBankTransfer;

    public static CheckoutResponseDto createResponseForBankTransfer(String orderId, String urlRedirect) {
        return CheckoutResponseDto
                .builder()
                .orderId(orderId)
                .message("Thanh toán chuyển khoản thành công, vui lòng chuyển khoản vào tài khoản ngân hàng của chúng tôi.")
                .urlRedirect(urlRedirect)
                .isBankTransfer(true)
        .build();
    }

    public static CheckoutResponseDto createResponseForCashOnDelivery(String orderId) {
        return CheckoutResponseDto
                .builder()
                .orderId(orderId)
                .message("Đặt hàng thành công, vui lòng thanh toán khi nhận hàng.")
                .isBankTransfer(false)
        .build();
    }
}
