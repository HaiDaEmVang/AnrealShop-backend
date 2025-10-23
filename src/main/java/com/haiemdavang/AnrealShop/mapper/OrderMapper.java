package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.order.*;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrderTrack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final AttributeMapper attributeMapper;
    private final AddressMapper addressMapper;

    public ProductOrderItemDto toOrderItemDto(OrderItem orderItem) {
        return ProductOrderItemDto.builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProductSku().getProduct().getId())
                .productSkuId(orderItem.getProductSku().getId())
                .productName(orderItem.getProductSku().getProduct().getName())
                .productImage(orderItem.getProductSku().getThumbnailUrl())
                .variant(attributeMapper.getAttribteString(orderItem.getProductSku().getAttributes()))
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .orderStatus(orderItem.getStatus().name())
                .cancelReason(orderItem.getCancelReason())
                .submitConfirmDate(orderItem.getUpdatedAt() != null ? orderItem.getUpdatedAt().toString() : "")
                .isReviewed(false)
                .build();
    }

    public Set<HistoryTrackDto> historyTrackDto(Set<ShopOrderTrack> shopOrderTracks){
        return shopOrderTracks.stream().map(track -> HistoryTrackDto.builder()
                .status(track.getStatus().name())
                .id(track.getId().toString())
                .title("NaN")
                .timestamp(track.getUpdatedAt())
                .build()).collect(Collectors.toSet());
    }

    public OrderDetailDto orderDetailDto(ShopOrder shopOrder) {
        return OrderDetailDto.builder()
                .orderId(shopOrder.getOrder().getId())
                .orderStatus(shopOrder.getStatus().name())
                .orderHistory(this.historyTrackDto(shopOrder.getTrackingHistory()))
                .shippingFee(shopOrder.getShippingFee())
                .customerName(shopOrder.getUser().getFullName())
                .customerPhone(shopOrder.getUser().getPhoneNumber())
                .customerAddress(shopOrder.getShippingAddress().getDetail())
                .isReviewed(false)
                .build();
    }


    public OrderItemDto toOrderItemDto(ShopOrder shopOrder, Set<OrderItem> orderItemsOfShopOrder) {
        if (shopOrder == null || orderItemsOfShopOrder.isEmpty()) return null;
        Set<ProductOrderItemDto> productOrderItemSet = orderItemsOfShopOrder.stream().map(this::toOrderItemDto).collect(Collectors.toSet());
        return OrderItemDto.builder()
                .shopOrderId(shopOrder.getId())
                .orderStatus(shopOrder.getStatus().name())
                .paymentMethod(shopOrder.getOrder().getPayment().getGateway().getValue())
                .customerName(shopOrder.getUser().getFullName())
                .customerImage(shopOrder.getUser().getAvatarUrl())
                .shippingMethod("hang nhe")
                .shippingId(shopOrder.getShipping() != null ? shopOrder.getShipping().getId(): null)
                .productOrderItemDtoSet(productOrderItemSet)
                .build();

    }

    public UserProductOrderItemDto toUserProductOrderItemDto(OrderItem orderItem) {
        return UserProductOrderItemDto.builder()
                .productId(orderItem.getProductSku().getProduct().getId())
                .productSkuId(orderItem.getProductSku().getId())
                .productName(orderItem.getProductSku().getProduct().getName())
                .productImage(orderItem.getProductSku().getThumbnailUrl())
                .variant(attributeMapper.getAttribteString(orderItem.getProductSku().getAttributes()))
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .orderStatus(orderItem.getStatus().name())
                .cancelReason(orderItem.getCancelReason())
                .isReviewed(false)
                .build();
    }

    public UserOrderItemDto toUserOrderItemDto(ShopOrder shopOrder, Set<OrderItem> orderItemsOfShopOrder) {
        if (shopOrder == null || orderItemsOfShopOrder.isEmpty()) return null;
        Set<UserProductOrderItemDto> productOrderItemSet = orderItemsOfShopOrder.stream().map(this::toUserProductOrderItemDto).collect(Collectors.toSet());
        Set<String> orderStatus = orderItemsOfShopOrder.stream().map(item -> item.getStatus().name()).collect(Collectors.toSet());
        return UserOrderItemDto.builder()
                .shopOrderId(shopOrder.getId())
                .orderStatus(orderStatus)
                .shopOrderName(shopOrder.getShop().getName())
                .shopOrderImage(shopOrder.getShop().getAvatarUrl())
                .productOrderItemDtoSet(productOrderItemSet)
                .totalPrice(shopOrder.getTotalAmount())
                .updateAt(shopOrder.getUpdatedAt())
                .build();
    }

    public UserOrderDetailDto toUserOrderDetailDto(ShopOrder shopOrder, Set<OrderItem> orderItemsOfShopOrder, UserAddress userAddress) {
        if (shopOrder == null || orderItemsOfShopOrder.isEmpty()) return null;

        List<ProductOrderItemDto> productItems = orderItemsOfShopOrder.stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());

        return UserOrderDetailDto.builder()
                .shopOrderId(shopOrder.getId())
                .shopOrderStatus(shopOrder.getStatus().name())
                .shopId(shopOrder.getShop().getId())
                .shopName(shopOrder.getShop().getName())
                .shopImage(shopOrder.getShop().getAvatarUrl())
                .productItems(productItems)
                .shippingFee(shopOrder.getShippingFee())
                .totalProductCost(shopOrder.getTotalAmount())
                .isReviewed(false)
                .address(addressMapper.toSimpleAddressDto(userAddress))
                .build();
    }
}
