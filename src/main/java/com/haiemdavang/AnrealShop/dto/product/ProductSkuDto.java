package com.haiemdavang.AnrealShop.dto.product;

import com.haiemdavang.AnrealShop.dto.sku.SkuAttributeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuDto {
    private String id;
    private String sku;
    private Long price;
    private int quantity;
    private String imageUrl;
    private List<SkuAttributeDto> attributes;
}
