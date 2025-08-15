package com.haiemdavang.AnrealShop.dto.checkout;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutInfoDto {
    public BaseShopDto shop;
    private List<CartItemDto> items;
    public int fee;
    public LocalDate leadTime;
    public String serviceName;
    public boolean isSuccess;
}
