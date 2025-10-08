package com.haiemdavang.AnrealShop.service;


import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;

public interface IShopService {
    boolean isExists(String id);
    Shop findById(String id);

    Shop findByEmailUser(String email);
}
