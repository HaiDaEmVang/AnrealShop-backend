package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CartShippingFee extends InfoShippingOrder {
    private String shopId;
}
