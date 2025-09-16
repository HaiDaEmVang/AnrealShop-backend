package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingItem {
    private String shopOrderId;
    private int countOrderItems;
    private String createdAt;

    private String customerId;
    private String customerName;
    private String customerPhone;

    private String shippingId;
    private String shippingMethod;
    private String shippingStatus;
    private String dayPickup;

    private String confirmationTime;
    private boolean isPrinted;
}
