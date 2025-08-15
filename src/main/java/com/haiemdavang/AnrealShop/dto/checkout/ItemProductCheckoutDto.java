package com.haiemdavang.AnrealShop.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemProductCheckoutDto {
    private String productSkuId;
    private int quantity;
}
