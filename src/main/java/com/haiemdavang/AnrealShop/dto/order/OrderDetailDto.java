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
public class OrderDetailDto {
    private String orderId;
    private String orderStatus;
    private Set<HistoryTrackDto> orderHistory;
    private Set<ShippingOrderItemDto> items;

    private Long totalProductCost;
    private Long totalShippingCost;
    private Long shippingFee;
    private Long shippingDiscount;

    private Long fixedFeeRate;
    private Long serviceFeeRate;
    private Long paymentFeeRate;

    private Long revenue;

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    private boolean isReviewed;
}
