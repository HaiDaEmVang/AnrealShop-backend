package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.order.HistoryTrackDto;
import com.haiemdavang.AnrealShop.modal.entity.shipping.ShippingTrack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentMapper {


//    public ShippingItem toShippingItems(Shipping shipping, ShopOrder shop) {
//        return ShippingItem.builder()
//                .shippingId(shipping.getId())
//                .shippingStatus(shipping.getStatus().name())
//                .dayPickup(shipping.getDayPickup().toString())
//                .isPrinted(shipping.isPrinted())
//
//                .shopOrderId(shop.getId())
//                .countOrderItems(shipping.getOrderItems().size())
//                .createdAt(shop.getCreatedAt().toString())
//
//                .customerId(shop.getUser().getId())
//                .customerName(shop.getUser().getFullName())
//                .customerPhone(shop.getUser().getPhoneNumber())
//
//                .confirmationTime(shipping.getCreatedAt().toString())
//                .shippingMethod("GHN")
//
//                .build();
//
//
//    }
    public List<HistoryTrackDto> toHistoryTrackDto(Collection<ShippingTrack> shippingTracks) {
        if (shippingTracks == null || shippingTracks.isEmpty()) return List.of();

        return shippingTracks.stream().map(this::toHistoryTrackDto)
                .sorted(Comparator.comparing(HistoryTrackDto::getTimestamp))
                .collect(Collectors.toList());
    }

    private HistoryTrackDto toHistoryTrackDto(ShippingTrack track) {
        return HistoryTrackDto.builder()
                .id(track.getId() != null ? track.getId().toString() : null)
                .status(track.getStatus() != null ? track.getStatus().name() : null)
                .timestamp(track.getUpdatedAt())
                .build();
    }


}
