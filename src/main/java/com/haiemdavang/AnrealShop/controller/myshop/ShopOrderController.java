package com.haiemdavang.AnrealShop.controller.myshop;

import com.haiemdavang.AnrealShop.dto.order.MyShopOrderListResponse;
import com.haiemdavang.AnrealShop.dto.order.OrderDetailDto;
import com.haiemdavang.AnrealShop.dto.order.OrderRejectRequest;
import com.haiemdavang.AnrealShop.dto.order.OrderStatusDto;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.service.order.IOrderItemService;
import com.haiemdavang.AnrealShop.service.order.IShopOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my-shop/orders")
public class ShopOrderController {

    private final IShopOrderService orderService;
    private final IOrderItemService orderItemService;

    @GetMapping("/meta-data")
    public ResponseEntity<Set<OrderStatusDto>> getFilterMetaData(
            @RequestParam(required = false, defaultValue = "") String orderCode,
            @RequestParam(required = false, defaultValue = "") String customerName,
            @RequestParam(required = false, defaultValue = "") String productName) {
        return ResponseEntity.ok(orderService.getShopFilterMetaData("", orderCode, customerName, productName));
    }

    @GetMapping
    public ResponseEntity<MyShopOrderListResponse> getListOrderItems(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "") String orderCode,
            @RequestParam(required = false, defaultValue = "") String customerName,
            @RequestParam(required = false, defaultValue = "") String productName,
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {
        return ResponseEntity.ok(orderService.getListOrderItems(page, limit, status, orderCode, customerName, productName, sortBy));
    }


    @GetMapping("/{shopOrderId}")
    public ResponseEntity<OrderDetailDto> getShopOrderById(@PathVariable String shopOrderId) {
        return ResponseEntity.ok(orderService.getShopOrder(shopOrderId));
    }

    @PutMapping("approve/{shopOrderId}")
    public ResponseEntity<?> approvalShopOrder(@PathVariable String shopOrderId) {
        orderService.approvalShopOrderById(shopOrderId);
        return ResponseEntity.ok(Map.of("message", "approval order successfully!"));
    }

    @PutMapping("reject-ids")
    public ResponseEntity<?> rejectOrderItems(@RequestBody OrderRejectRequest orderRejectRequest) {
        orderItemService.rejectOrderItemByIds(orderRejectRequest, CancelBy.SHOP);
        return ResponseEntity.ok(Map.of("message", "reject orderItems successfully!"));
    }

    @PutMapping("reject-id/{orderItemId}")
    public ResponseEntity<?> rejectOrderItems(@PathVariable String orderItemId, @RequestBody String reason) {
        orderService.rejectOrderItemById(orderItemId, reason, CancelBy.SHOP);
        return ResponseEntity.ok(Map.of("message", "reject orderItem successfully!"));
    }
}
