package com.haiemdavang.AnrealShop.schedule;

import com.haiemdavang.AnrealShop.service.order.IShopOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSchedule {
    private final IShopOrderService orderService;

//    @Scheduled(cron = "0/5 * * * * ?")
    public void shipperGiveOrder() {
        orderService.confirmShipmentOrders();
    }
}
