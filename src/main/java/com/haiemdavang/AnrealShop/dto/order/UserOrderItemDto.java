package com.haiemdavang.AnrealShop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderItemDto {
    Set<UserProductOrderItemDto> productOrderItemDtoSet;

    private String shopOrderId;
    private String shopOrderName;
    private String shopOrderImage;
    private Set<String> orderStatus;
    private Long totalPrice;
    private LocalDateTime updateAt;
}
