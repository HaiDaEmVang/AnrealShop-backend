package com.haiemdavang.AnrealShop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderItemDto {
    private String orderItemId;
    private String productId;
    private String productSkuId;
    private String productName;
    private String productImage;
    private String variant;
    private int quantity;
    private Long price;
    private String orderStatus;

    private String cancelReason;
    private boolean isReviewed;
}
