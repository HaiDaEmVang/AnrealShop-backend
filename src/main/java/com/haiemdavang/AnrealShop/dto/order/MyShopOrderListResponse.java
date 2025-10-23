package com.haiemdavang.AnrealShop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class MyShopOrderListResponse {
    private Long totalCount;
    private Integer totalPages;
    private Integer currentPage;
    private List<OrderItemDto> orderItemDtoSet;
}