package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;

import java.util.Set;

public interface IShopOrderService {

    void insertShopOrderTrack(Set<ShopOrder> shopOrders, Order newOrder);
}
