//package com.haiemdavang.AnrealShop.controller.user;
//
//import com.haiemdavang.AnrealShop.dto.checkout.CheckoutInfoDto;
//import com.haiemdavang.AnrealShop.dto.checkout.CheckoutRequestDto;
//import com.haiemdavang.AnrealShop.dto.checkout.CheckoutResponseDto;
//import com.haiemdavang.AnrealShop.service.ICheckoutService;
//import com.haiemdavang.AnrealShop.service.order.IOrderService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/user/order")
//public class OrderController {
//    private final IOrderService orderService;
//
//    @GetMapping
//    public ResponseEntity<List<CheckoutInfoDto>> getOrder() {
//        return ResponseEntity.ok(checkoutService.getListCartCheckout());
//    }
//
//    @PostMapping
//    public ResponseEntity<CheckoutResponseDto> checkout(@RequestBody @Valid CheckoutRequestDto requestDto) {
//        checkoutService.validateItems(requestDto.getItems());
//        return ResponseEntity.ok(checkoutService.checkout(requestDto));
//    }
//
//}
