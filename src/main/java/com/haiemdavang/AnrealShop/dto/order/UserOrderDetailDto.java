package com.haiemdavang.AnrealShop.dto.order;

import com.haiemdavang.AnrealShop.dto.address.SimpleAddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDetailDto {
    private String shopOrderId;
    private String shopOrderStatus;

    private String shopId;
    private String shopName;
    private String shopImage;

    @Builder.Default
    private List<HistoryTrackDto> orderHistory = new ArrayList<>();
    @Builder.Default
    private List<ProductOrderItemDto> productItems = new ArrayList<>();

    private Long totalProductCost;
    private Long totalShippingCost;
    private Long shippingFee;
    private Long shippingDiscount;

    private Long totalCost;

    private boolean isReviewed;

    private SimpleAddressDto address;
}
