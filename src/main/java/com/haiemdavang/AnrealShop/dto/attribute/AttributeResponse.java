package com.haiemdavang.AnrealShop.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeResponse {
    List<AttributeDto> attribute; // branch, original, etc.
    List<ProductAttribute> attributeForSku; //size, color, etc.
}
