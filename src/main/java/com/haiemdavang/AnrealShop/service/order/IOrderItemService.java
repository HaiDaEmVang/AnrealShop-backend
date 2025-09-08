package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.dto.order.OrderRejectRequest;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItemTrack;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface IOrderItemService {

    void insertOrderItemTrack(Set<OrderItem> orderItems, Order newOrder);

    OrderItem ConfirmOrderItem(OrderItem orderItem, OrderItemTrack newStatus);

    @Transactional
    void confirmOrderItems(Set<OrderItem> orderItems, OrderTrackStatus newStatus);

    List<OrderItem> getListOrderItems(Set<String> idShopOrders, String productName, String status);

    void rejectOrderItemByIds(OrderRejectRequest orderRejectRequest, CancelBy cancelBy);

    ShopOrder rejectOrderItemById(String orderItemId, String reason, CancelBy cancelBy);
}
