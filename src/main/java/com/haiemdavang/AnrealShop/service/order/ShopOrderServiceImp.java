package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.dto.order.*;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.mapper.OrderMapper;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrderTrack;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import com.haiemdavang.AnrealShop.repository.order.ShopOrderRepository;
import com.haiemdavang.AnrealShop.repository.order.ShopOrderSpecification;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopOrderServiceImp implements IShopOrderService {
    private final ShopOrderRepository shopOrderRepository;
    private final SecurityUtils securityUtils;
    private final OrderMapper orderMapper;
    private final IOrderItemService orderItemService;


    @Override
    @Transactional
    public void insertShopOrderTrack(Set<ShopOrder> shopOrders, Order newOrder) {
        for (ShopOrder shopOrder : shopOrders) {
            ShopOrderTrack shopOrderTrack = new ShopOrderTrack(shopOrder);
            shopOrder.addTrackingHistory(shopOrderTrack);
        }
        shopOrderRepository.saveAll(shopOrders);
    }

    @Override
    public Set<OrderStatusDto> getShopFilterMetaData(String shopId, String orderCode, String customerName, String productName) {
        LocalDateTime now = LocalDate.now().atTime(23, 59, 59);
        shopId = securityUtils.getCurrentUserShop().getId();
        Specification<ShopOrder> spec = ShopOrderSpecification.filter(shopId, now.minusMonths(2), now, orderCode, customerName, productName, null);
        List<ShopOrder> orders = shopOrderRepository.findAll(spec);

        Map<ShopOrderStatus, Integer> statusMap = orders.stream().collect(Collectors.toMap(
                ShopOrder::getStatus,
                so -> 1,
                Integer::sum
        ));

        Set<OrderStatusDto> result = new HashSet<>();
        int total = 0;
        for (ShopOrderStatus data : ShopOrderStatus.values()){
            if (data.equals(ShopOrderStatus.INIT_PROCESSING)) continue;
            Integer count = statusMap.get(data);
            if (count == null){
                result.add(OrderStatusDto.createNewOrderStatusDto(data));
            }else {
                total += count;
                result.add(OrderStatusDto.convertToOrderStatsDto(data, count));
            }
        }
        result.add(OrderStatusDto.builder().id("ALL").name("Tất cả").count(total).build());

        return result;
    }

    @Override
    public MyShopOrderListResponse getListOrderItems(int page, int limit, String status, String orderCode, String customerName, String productName, String sortBy) {
        LocalDateTime now =  LocalDate.now().atTime(23, 59, 59);
        String shopId = securityUtils.getCurrentUserShop().getId();
        Specification<ShopOrder> orderSpecification = ShopOrderSpecification.filter(shopId, now.minusMonths(2), now, orderCode, customerName, productName, status);
        Pageable pageable = PageRequest.of(page, limit, ApplicationInitHelper.getSortBy(sortBy));

        Page<ShopOrder> shopOrders = shopOrderRepository.findAll(orderSpecification, pageable);
        Set<String> idShopOrders = shopOrders.stream().map(ShopOrder::getId).collect(Collectors.toSet());
        Map<String, ShopOrder> mapShopOrders = shopOrders.stream().collect(Collectors.toMap(ShopOrder::getId, so -> so));

        Set<OrderItemDto> orderItemDtoSet = new HashSet<>();

        List<OrderItem> orderItems = orderItemService.getListOrderItems(idShopOrders, productName, status);
        Map<String, Set<OrderItem>> mapOrderItems = orderItems.stream().collect(
                Collectors.groupingBy(oi -> oi.getShopOrder().getId(), Collectors.toSet())
        ).entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
        );

        for (String idShopOrder : mapOrderItems.keySet()){
            ShopOrder shopOrder = mapShopOrders.get(idShopOrder);
            Set<OrderItem> orderItemsOfShopOrder = mapOrderItems.get(idShopOrder);
            if (shopOrder == null || orderItemsOfShopOrder == null) continue;
            OrderItemDto orderItemDto = orderMapper.toOrderItemDto(shopOrder, orderItemsOfShopOrder);
            orderItemDtoSet.add(orderItemDto);
        }

        return  MyShopOrderListResponse.builder()
                .currentPage(shopOrders.getNumber())
                .totalPages(shopOrders.getTotalPages())
                .totalCount(shopOrders.getTotalElements())
                .orderItemDtoSet(orderItemDtoSet)
                .build();
    }

    @Override
    public OrderDetailDto getShopOrder(String shopOrderId) {
        if (shopOrderId == null || shopOrderId.isEmpty() || !shopOrderRepository.existsById(shopOrderId)) {
            throw new BadRequestException("ORDER_NOT_FOUND");
        }

        ShopOrder shopOrder = shopOrderRepository.findWithFullInfoById(shopOrderId);

        OrderDetailDto orderDetailDto = orderMapper.orderDetailDto(shopOrder);

        long totalProductCost = shopOrder.getTotalAmount() - shopOrder.getShippingFee();
        double SHIPPING_FEE_RATE = 0.2;
        Long shippingDiscount = shopOrder.getShippingFee() == 0L ? 0L : (long) (shopOrder.getShippingFee() * SHIPPING_FEE_RATE);
        Long shippingFee = orderDetailDto.getShippingFee();

        orderDetailDto.setTotalProductCost(totalProductCost);
        orderDetailDto.setShippingDiscount(shippingDiscount);
        orderDetailDto.setTotalShippingCost(Math.max(0L, shippingFee - shippingDiscount));
        Long FIXED_FEE = 30L;
        orderDetailDto.setFixedFeeRate(FIXED_FEE);
        Long SERVICE_FEE = 42L;
        orderDetailDto.setServiceFeeRate(SERVICE_FEE);
        Long PAYMENT_FEE = 74L;
        orderDetailDto.setPaymentFeeRate(PAYMENT_FEE);
        orderDetailDto.setRevenue(totalProductCost - Math.round(totalProductCost * (SHIPPING_FEE_RATE + FIXED_FEE + SERVICE_FEE)/100));

        return orderDetailDto;
    }

    @Transactional
    @Override
    public void confirmShopOrders(Set<ShopOrder> shopOrders, ShopOrderStatus newStatus) {
        Set<ShopOrder> results = new HashSet<>();
        for (ShopOrder item : shopOrders) {
            ShopOrderTrack latestTrack = item.getTrackingHistory().stream()
                    .max(Comparator.comparing(ShopOrderTrack::getUpdatedAt))
                    .orElseThrow(() -> new BadRequestException("NO_TRACKING_HISTORY"));

            if (latestTrack.getStatus().equals(newStatus))
                break;

            ShopOrderTrack ShopOrderTrack = new ShopOrderTrack(item, newStatus, LocalDateTime.now());

            item.addTrackingHistory(ShopOrderTrack);

            item.setStatus(newStatus);

            results.add(item);
        }

        shopOrderRepository.saveAll(results);
    }

    @Override
    @Transactional
    public void approvalShopOrderById(String shopOrderId) {
        if (shopOrderId == null || shopOrderId.isEmpty() || !shopOrderRepository.existsById(shopOrderId)) {
            throw new BadRequestException("ORDER_NOT_FOUND");
        }

        ShopOrder shopOrder = shopOrderRepository.findWithOrderItemById(shopOrderId);

        shopOrder.setStatus(ShopOrderStatus.PREPARING);

        shopOrder.getOrderItems().stream()
                .filter(ot -> ot.getStatus().equals(OrderTrackStatus.PENDING_CONFIRMATION))
                .forEach(ot -> ot.setStatus(OrderTrackStatus.PREPARING));

        shopOrderRepository.save(shopOrder);
    }

    @Override
    @Transactional
    public void rejectOrderItemById(String orderItemId, String reason, CancelBy cancelBy) {
        ShopOrder shopOrder = orderItemService.rejectOrderItemById(orderItemId, reason, cancelBy);
        handleMapStatus(shopOrder);
    }

    private  void handleMapStatus(ShopOrder shopOrder) {
        if (shopOrder.getOrderItems().stream().allMatch(ot -> ot.getStatus().equals(OrderTrackStatus.CANCELED))) {
            shopOrder.setStatus(ShopOrderStatus.CLOSED);
        }

        shopOrderRepository.save(shopOrder);
    }


}
