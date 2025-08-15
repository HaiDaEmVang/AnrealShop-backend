package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrderTrack;
import com.haiemdavang.AnrealShop.repository.order.ShopOrderRepository;
import com.haiemdavang.AnrealShop.service.IShopOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShopOrderServiceImp implements IShopOrderService {
    private final ShopOrderRepository shopOrderRepository;

    @Override
    @Transactional
    public void insertShopOrderTrack(Set<ShopOrder> shopOrders, Order newOrder) {
        for (ShopOrder shopOrder : shopOrders) {
            ShopOrderTrack shopOrderTrack = new ShopOrderTrack(shopOrder);
            shopOrder.addTrackingHistory(shopOrderTrack);
        }
        shopOrderRepository.saveAll(shopOrders);
    }
}
