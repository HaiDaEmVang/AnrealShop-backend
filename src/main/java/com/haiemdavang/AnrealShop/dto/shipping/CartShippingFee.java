package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CartShippingFee extends InfoShippingOrder {
    private String shopId;
}
