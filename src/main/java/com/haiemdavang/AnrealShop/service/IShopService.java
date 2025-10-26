package com.haiemdavang.AnrealShop.service;


import com.haiemdavang.AnrealShop.dto.shop.ShopDto;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import jakarta.validation.Valid;

public interface IShopService {
    Shop findById(String id);

    Shop findByEmailUser(String email);

    boolean isExistByUserId(String id);

    ShopDto findDtoByEmailUser(String username);

    ShopDto registerUser(String username, @Valid String shopName);
}
