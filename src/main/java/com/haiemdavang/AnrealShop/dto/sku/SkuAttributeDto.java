package com.haiemdavang.AnrealShop.dto.sku;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuAttributeDto {
    private String id;
    private String attributeKeyName;
    private String value;
}
