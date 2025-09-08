package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.dto.order.MyShopOrderListResponse;
import com.haiemdavang.AnrealShop.dto.order.OrderDetailDto;
import com.haiemdavang.AnrealShop.dto.order.OrderStatusDto;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface IShopOrderService {
    OrderDetailDto getShopOrder(String shopOrderId);

    Set<OrderStatusDto> getShopFilterMetaData(String shopId, String orderCode, String customerName, String productName );

    void insertShopOrderTrack(Set<ShopOrder> shopOrders, Order newOrder);

    MyShopOrderListResponse getListOrderItems(int page, int limit, String status, String orderCode, String customerName, String productName, String sortBy);

    @Transactional
    void confirmShopOrders(Set<ShopOrder> shopOrders, ShopOrderStatus newStatus);

    void approvalShopOrderById(String shopOrderId);

    void rejectOrderItemById(String orderItemId, String reason, CancelBy cancelBy);
}