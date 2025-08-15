package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class InfoShippingOrder {
    public int fee;
    public LocalDate leadTime;
    public String serviceName;
    public boolean isSuccess;

    public static InfoShippingOrder createSuccessInfoShippingOrder(int fee, LocalDate leadTime, String serviceName) {
        return InfoShippingOrder.builder()
                .fee(fee)
                .leadTime(leadTime)
                .serviceName(serviceName)
                .isSuccess(true)
                .build();
    }

    public static InfoShippingOrder createFailedInfoShippingOrder() {
        return InfoShippingOrder.builder()
                .isSuccess(false)
                .fee(0)
                .build();
    }
}
