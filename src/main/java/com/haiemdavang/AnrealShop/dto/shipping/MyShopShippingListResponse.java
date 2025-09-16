package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class MyShopShippingListResponse {
    private Long totalCount;
    private Integer totalPages;
    private Integer currentPage;
    private Set<ShippingItem> orderItemDtoSet;
}