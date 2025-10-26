package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.shipping.HistoryShipping;
import com.haiemdavang.AnrealShop.dto.shipping.HistoryShippingNote;
import com.haiemdavang.AnrealShop.dto.shipping.ShippingItem;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.entity.shipping.ShippingTrack;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentMapper {


    public ShippingItem toShippingItems(Shipping shipping, ShopOrder shop) {
        return ShippingItem.builder()
                .shippingId(shipping.getId())
                .shippingStatus(shipping.getStatus().name())
                .dayPickup(shipping.getDayPickup().toString())
                .isPrinted(shipping.isPrinted())

                .shopOrderId(shop.getId())
                .countOrderItems(shop.getOrderItems().size())
                .createdAt(shop.getCreatedAt().toString())

                .customerId(shop.getUser().getId())
                .customerName(shop.getUser().getFullName())
                .customerPhone(shop.getUser().getPhoneNumber())

                .confirmationTime(shipping.getCreatedAt().toString())
                .shippingMethod("GHN")
                .build();
    }


    public List<HistoryShipping> toHistoryTrackDto(Collection<ShippingTrack> shippingTracks) {
        if (shippingTracks == null || shippingTracks.isEmpty()) return List.of();
        Map<ShippingStatus, List<ShippingTrack>> historyTrackMap = shippingTracks.stream()
                .collect(Collectors.groupingBy(ShippingTrack::getStatus));

        return Arrays.stream(ShippingStatus.values()).map(item -> this.toHistoryShippingNote(historyTrackMap.get(item)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private HistoryShipping toHistoryShippingNote(List<ShippingTrack> shippingTracks) {
        if (shippingTracks == null || shippingTracks.isEmpty()) return null;
        List<HistoryShippingNote> notes = shippingTracks.stream()
                .sorted(Comparator.comparing( ShippingTrack::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(item -> HistoryShippingNote.builder().content(item.getNote()).timestamp(item.getUpdatedAt()).build()).toList();
        return HistoryShipping.builder()
                .status(shippingTracks.get(0).getStatus().name())
                .notes(notes)
                .build();
    }


}
