package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import com.haiemdavang.AnrealShop.dto.shop.ShopDto;
import com.haiemdavang.AnrealShop.tech.elasticsearch.document.EsShop;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import org.springframework.stereotype.Service;

@Service
public class ShopMapper {
    public BaseShopDto toBaseShopDto(Shop shop) {
        if (shop == null) {
            return null;
        }

        return BaseShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .avatarUrl(shop.getAvatarUrl())
                .build();
    }

    public BaseShopDto toBaseShopDto(EsShop shop) {
        if (shop == null) {
            return null;
        }

        return BaseShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();
    }

    public EsShop toEsShop(BaseShopDto shop) {
        if (shop == null) {
            return null;
        }

        return EsShop.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();
    }

    public ShopDto toShopDto(Shop shop) {
        if (shop == null) return null;
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .avatarUrl(shop.getAvatarUrl())
                .shopUrl(shop.getUrlSlug())
                .build();
    }
}
