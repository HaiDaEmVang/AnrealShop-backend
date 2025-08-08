package com.haiemdavang.AnrealShop.dto.cart;

import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CartDto {
    private BaseShopDto shop; // product -> shop
    private Set<CartItemDto> items;
}
