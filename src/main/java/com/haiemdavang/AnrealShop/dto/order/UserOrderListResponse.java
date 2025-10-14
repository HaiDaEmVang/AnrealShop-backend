package com.haiemdavang.AnrealShop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class UserOrderListResponse {
    private Long totalCount;
    private Integer totalPages;
    private Integer currentPage;
    private Set<UserOrderItemDto> orderItemDtoSet;
}