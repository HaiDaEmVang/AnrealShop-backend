package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.order.HistoryTrackDto;
import com.haiemdavang.AnrealShop.dto.order.OrderDetailDto;
import com.haiemdavang.AnrealShop.dto.order.OrderItemDto;
import com.haiemdavang.AnrealShop.dto.order.ProductOrderItemDto;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrderTrack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final AttributeMapper attributeMapper;

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
        Set<String> orderStatus = orderItemsOfShopOrder.stream().map(item -> item.getStatus().name()).collect(Collectors.toSet());
        return OrderItemDto.builder()
                .shopOrderId(shopOrder.getId())
                .orderStatus(orderStatus)
                .paymentMethod(shopOrder.getOrder().getPayment().getGateway().getValue())
                .customerName(shopOrder.getUser().getFullName())
                .customerImage(shopOrder.getUser().getAvatarUrl())
                .shippingMethod("hang nhe")
                .shippingId("123456")
                .productOrderItemDtoSet(productOrderItemSet)
                .build();

    }
}
