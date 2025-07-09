package com.haiemdavang.AnrealShop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyShopProductListResponse {
    private Long totalCount;
    private Integer totalPages;
    private Integer currentPage;
    private List<MyShopProductDto> products;
}