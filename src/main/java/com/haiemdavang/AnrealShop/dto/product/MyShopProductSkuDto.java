package com.haiemdavang.AnrealShop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyShopProductSkuDto {
    private String id;
    private String sku;
    private String imageUrl;
    private Long price;
    private Integer quantity;
    private Integer sold;
    private String createdAt;
}