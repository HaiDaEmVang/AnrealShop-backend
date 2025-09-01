package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItemTrack;
import com.haiemdavang.AnrealShop.repository.order.OrderItemRepository;
import com.haiemdavang.AnrealShop.service.IOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImp implements IOrderItemService {
    private final OrderItemRepository orderItemRepository;


    @Override
    @Transactional
    public void insertOrderItemTrack(Set<OrderItem> orderItems, Order newOrder) {
        for (OrderItem orderItem : orderItems) {
            OrderItemTrack orderItemTrack = new OrderItemTrack(orderItem);
            orderItem.addTrackingHistory(orderItemTrack);
        }
        orderItemRepository.saveAll(orderItems);
    }
}
