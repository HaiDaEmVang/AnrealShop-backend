package com.haiemdavang.AnrealShop.controller.myshop;

import com.haiemdavang.AnrealShop.dto.order.MyShopOrderListResponse;
import com.haiemdavang.AnrealShop.dto.order.OrderDetailDto;
import com.haiemdavang.AnrealShop.dto.order.OrderRejectRequest;
import com.haiemdavang.AnrealShop.dto.order.OrderStatusDto;
import com.haiemdavang.AnrealShop.dto.order.search.ModeType;
import com.haiemdavang.AnrealShop.dto.order.search.OrderCountType;
import com.haiemdavang.AnrealShop.dto.order.search.PreparingStatus;
import com.haiemdavang.AnrealShop.dto.order.search.SearchType;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import com.haiemdavang.AnrealShop.service.order.IOrderItemService;
import com.haiemdavang.AnrealShop.service.order.IShopOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my-shop/orders")
public class MyShopOrderController {

    private final IShopOrderService orderService;
    private final IOrderItemService orderItemService;

    @GetMapping("/meta-data")
    public ResponseEntity<Set<OrderStatusDto>> getFilterMetaData(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "order_code") SearchType searchType) {
        return ResponseEntity.ok(orderService.getShopFilterMetaData("", search, searchType));
    }

    @GetMapping
    public ResponseEntity<MyShopOrderListResponse> getListOrderItems(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "home") ModeType mode,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "order_code") SearchType searchType,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate confirmSD,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate confirmED,
            @RequestParam(required = false, defaultValue = "all") OrderCountType orderType,
            @RequestParam(required = false, defaultValue = "all") PreparingStatus preparingStatus,
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {

        LocalDateTime confirmSDTime = null;
        if (confirmSD!= null) {
            confirmSDTime = confirmSD.atTime(0,0,0);
        }
        LocalDateTime confirmEDTime = null;
        if (confirmED!= null) {
            confirmEDTime = confirmED.atTime(23,59,59);
        }
        return ResponseEntity.ok(orderService.getListOrderItems(page, limit, mode, status, search, searchType, confirmSDTime, confirmEDTime, orderType, preparingStatus, sortBy));
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

    @PutMapping("approvals")
    public ResponseEntity<?> approvalShopOrders(@RequestBody List<String> shopOrderIds) {
        orderService.updateStatus(shopOrderIds, ShopOrderStatus.CONFIRMED);
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
