package com.haiemdavang.AnrealShop.controller.user;

import com.haiemdavang.AnrealShop.dto.order.UserOrderDetailDto;
import com.haiemdavang.AnrealShop.dto.order.UserOrderListResponse;
import com.haiemdavang.AnrealShop.dto.order.search.SearchType;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.service.order.IShopOrderService;
import com.haiemdavang.AnrealShop.service.order.IUserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/orders")
public class OrderController {
    private final IUserOrderService orderService;
    private final IShopOrderService shopOrderService;


    @GetMapping
    public ResponseEntity<UserOrderListResponse> getListOrderItems(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "INIT_PROCESSING") String status,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "order_code") SearchType searchType,
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {

        return ResponseEntity.ok(orderService.getListOrderItems(page, limit, status, search, searchType, sortBy));
    }

    @PutMapping("reject/{shopOrderId}")
    public ResponseEntity<?> rejectOrderItems(@PathVariable String shopOrderId, @RequestBody String reason) {
        shopOrderService.rejectOrderById(shopOrderId, reason, CancelBy.CUSTOMER);
        return ResponseEntity.ok(Map.of("message", "reject orderItem successfully!"));
    }

    @GetMapping("/{shopOrderId}")
    public ResponseEntity<UserOrderDetailDto> getShopOrderById(@PathVariable String shopOrderId) {
        return ResponseEntity.ok(shopOrderService.getShopOrderForUser(shopOrderId));
    }

}
