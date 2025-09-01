package com.haiemdavang.AnrealShop.service.order;

import com.haiemdavang.AnrealShop.dto.checkout.ItemProductCheckoutDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutRequestDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutResponseDto;
import com.haiemdavang.AnrealShop.dto.payment.PaymentRequestDto;
import com.haiemdavang.AnrealShop.dto.payment.PaymentResponseDto;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.order.Payment;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.PaymentStatus;
import com.haiemdavang.AnrealShop.modal.enums.PaymentType;
import com.haiemdavang.AnrealShop.repository.order.OrderRepository;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.*;
import com.haiemdavang.AnrealShop.service.payment.IPaymentService;
import com.haiemdavang.AnrealShop.service.payment.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final IShopOrderService shopOrderService;
    private final IOrderItemService orderItemService;
    private final IPaymentService paymentService;
    private final IShipmentService shipmentService;
    private final IProductService productService;
    private final VNPayService vnPayService;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public CheckoutResponseDto createOrderBankTran(CheckoutRequestDto requestDto, UserAddress userAddress, String ipAddress) {
        Order newOrder = this.createNewOrder(requestDto, userAddress, PaymentType.BANK_TRANSFER);

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .orderId(newOrder.getId())
                .amount(newOrder.getGrandTotalAmount())
                .orderInfo("Nhap thong tin vao nhe hai")
                .build();

        return CheckoutResponseDto.createResponseForBankTransfer(newOrder.getId(), vnPayService.createPaymentUrl(paymentRequestDto, ipAddress));
    }

    @Override
    public CheckoutResponseDto createOrderCOD(CheckoutRequestDto requestDto, UserAddress userAddress) {
        Order newOrder = this.createNewOrder(requestDto, userAddress, PaymentType.COD);

        return CheckoutResponseDto.createResponseForCashOnDelivery(newOrder.getId());
    }

    @Override
    @Transactional
    public void handleSuccessfulPayment(String orderId) {
        Order order = orderRepository.findWithPaymentById(orderId)
                .orElseThrow(() -> new BadRequestException("ORDER_NOT_FOUND"));

        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.COMPLETED);

        paymentService.updatePayment(payment);
    }

    @Override
    public void handleFailedPayment(String orderId, String responseCode) {
//        phat trien len nghe hai
    }

    @Override
    public PaymentResponseDto getPaymentResult(String orderId) {
        Order order = orderRepository.findWithPaymentById(orderId)
                .orElseThrow(() -> new BadRequestException("ORDER_NOT_FOUND"));

        Payment payment = order.getPayment();

        return PaymentResponseDto.builder()
                .orderId(order.getId())
                .amount(order.getGrandTotalAmount())
                .paymentGateway(payment.getGateway())
                .paymentStatus(payment.getStatus())
                .paymentMethod(payment.getType())
                .isTransfer(payment.getType().equals(PaymentType.BANK_TRANSFER))
                .orderDate(order.getCreatedAt())
                .orderDateExpiration(payment.getExpireAt())
                .build();
    }

    private Order createNewOrder(CheckoutRequestDto requestDto, UserAddress userAddress, PaymentType paymentType) {
        User user = securityUtils.getCurrentUser();

        Map<String, Integer> itemRequests = requestDto.getItems().stream().collect(
                Collectors.toMap(ItemProductCheckoutDto::getProductSkuId, ItemProductCheckoutDto::getQuantity));
        List<ProductSku> productSkus = productService.findByProductSkuIdIn(itemRequests.keySet());
        Map<ProductSku, Integer> mapProductSkuWithQuantityOrder = productSkus.stream().collect(
                Collectors.toMap(t -> t, t -> itemRequests.get(t.getId())));

        Order order = Order.builder()
                .user(user)
                .shippingAddress(userAddress)
                .build();

//        Set<OrderItem> orderItems = new HashSet<>();
        long subTotalAmount = 0L;
        for (ProductSku productSku : productSkus) {
            Product product = productSku.getProduct();
            Integer quantity = itemRequests.get(productSku.getId());

            subTotalAmount += product.getPrice() * quantity;
            OrderItem orderItem = OrderItem.builder()
                    .productSku(productSku)
                    .quantity(quantity)
                    .price(product.getPrice())
                    .build();
//            orderItems.add(orderItem);
            order.addOrderItem(orderItem);
        }

        Map<ShopAddress, Integer> mapFeeForShops = shipmentService.getShippingFee(userAddress, mapProductSkuWithQuantityOrder);
        long totalShippingFee = 0L;

        for (ShopAddress shopAddress : mapFeeForShops.keySet()) {
            ShopOrder shopOrder = ShopOrder.builder()
                    .shop(shopAddress.getShop())
                    .user(user).build();

            Integer shippingFee = mapFeeForShops.get(shopAddress);

            long totalForShop = 0L;

            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getProductSku().getProduct().getShop().equals(shopAddress.getShop())){
                    totalForShop += orderItem.getQuantity() * orderItem.getPrice();
                    shopOrder.addOrderItems(orderItem);
                }
            }
            totalShippingFee += shippingFee;

            shopOrder.setShippingFee(shippingFee);
            shopOrder.setTotalAmount(totalForShop);
            shopOrder.setShippingAddress(shopAddress);

            order.addShopOrder(shopOrder);
        }


        long grandTotalAmount = subTotalAmount + totalShippingFee;
        Payment payment = paymentService.createPayment(grandTotalAmount, requestDto.getPaymentGateway(), paymentType);

        order.setPayment(payment);
        order.setSubTotalAmount(subTotalAmount);
        order.setTotalShippingFee(totalShippingFee);
        order.setGrandTotalAmount(grandTotalAmount);

        Order newOrder = orderRepository.save(order);
        shopOrderService.insertShopOrderTrack(newOrder.getShopOrders(), newOrder);
        orderItemService.insertOrderItemTrack(newOrder.getOrderItems(), newOrder);
//        productService.decreaseProductSkuQuantity(orderItems);
        return newOrder;
    }
}
