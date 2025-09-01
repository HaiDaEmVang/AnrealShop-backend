package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;

import java.util.Set;

public interface IOrderItemService {

    void insertOrderItemTrack(Set<OrderItem> orderItems, Order newOrder);
}
