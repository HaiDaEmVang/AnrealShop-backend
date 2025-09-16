package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.dto.order.MyShopOrderListResponse;
import com.haiemdavang.AnrealShop.dto.order.OrderDetailDto;
import com.haiemdavang.AnrealShop.dto.order.OrderStatusDto;
import com.haiemdavang.AnrealShop.dto.order.search.ModeType;
import com.haiemdavang.AnrealShop.dto.order.search.OrderCountType;
import com.haiemdavang.AnrealShop.dto.order.search.PreparingStatus;
import com.haiemdavang.AnrealShop.dto.order.search.SearchType;
import com.haiemdavang.AnrealShop.dto.shipping.search.SearchTypeShipping;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IShopOrderService {
    OrderDetailDto getShopOrder(String shopOrderId);

    Set<OrderStatusDto> getShopFilterMetaData(String shopId, String search, SearchType searchType);

    void insertShopOrderTrack(Set<ShopOrder> shopOrders, Order newOrder);

    @Transactional
    void confirmShopOrders(Set<ShopOrder> shopOrders, ShopOrderStatus newStatus);

    void approvalShopOrderById(String shopOrderId);

    void rejectOrderItemById(String orderItemId, String reason, CancelBy cancelBy);

    MyShopOrderListResponse getListOrderItems(int page, int limit, ModeType mode, String status, String search, SearchType searchType, LocalDateTime confirmSD, LocalDateTime confirmED, OrderCountType orderType, PreparingStatus preparingStatus, String sortBy);

    List<ShopOrder> getShopOrderByShippingIds(Set<String> shippingIds, String search, SearchTypeShipping searchType);
}