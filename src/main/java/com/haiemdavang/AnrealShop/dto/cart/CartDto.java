package com.haiemdavang.AnrealShop.dto.cart;

import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartDto {
    private BaseShopDto shop;
    private List<CartItemDto> items;
}
