package com.haiemdavang.AnrealShop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    Set<ProductOrderItemDto> productOrderItemDtoSet;

    private String shopOrderId;
    private String orderStatus;
    private String paymentMethod;
    private String customerName;
    private String customerImage;
    private String shippingMethod;
    private String shippingId;
}
