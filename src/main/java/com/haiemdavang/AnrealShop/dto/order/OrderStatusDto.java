package com.haiemdavang.AnrealShop.dto.order;

import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDto {
    private String id;
    private String name;
    private int count;

    public static OrderStatusDto createNewOrderStatusDto(ShopOrderStatus data) {
        return OrderStatusDto.builder().count(0).name(data.getDisplayName()).id(data.name()).build();
    }

    public static OrderStatusDto convertToOrderStatsDto(ShopOrderStatus data, Integer count) {
        return OrderStatusDto.builder().count(count).name(data.getDisplayName()).id(data.name()).build();
    }
}
