package com.haiemdavang.AnrealShop.dto.shipping.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutShippingFee {
    private String userAddressId;
    private Map<String, Integer> checkoutItems;
}
