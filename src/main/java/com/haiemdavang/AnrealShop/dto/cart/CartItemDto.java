package com.haiemdavang.AnrealShop.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {
    private String productId;
    private int quantity;
}
