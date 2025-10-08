package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.repository.ShopRepository;
import com.haiemdavang.AnrealShop.service.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImp implements IShopService {
    private final ShopRepository shopRepository;

    @Override
    public boolean isExists(String id) {
        return !shopRepository.existsById(id);
    }

    @Override
    public Shop findById(String id) {
        return shopRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("SHOP_NOT_FOUND"));
    }

    @Override
    public Shop findByEmailUser(String email) {
        return shopRepository.findByUserEmail(email)
                .orElseThrow(()-> new BadRequestException("SHOP_NOT_FOUND"));
    }

}
