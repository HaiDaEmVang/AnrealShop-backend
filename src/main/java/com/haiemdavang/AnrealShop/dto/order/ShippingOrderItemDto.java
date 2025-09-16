package com.haiemdavang.AnrealShop.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ShippingOrderItemDto {
    private String id;
    private String shippingId;
    private String shippingMethod;
    private String shippingBrand;

    private String shipperName;
    private String shipperPhone;

    private String productImage;
    private String productName;
    private int productQuantity;

    private List<HistoryTrackDto> shippingHistory;
}
