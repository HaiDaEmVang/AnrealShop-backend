package com.haiemdavang.AnrealShop.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeSingleValueDto {
    private String attributeKeyName;
    private String attributeKeyDisplay;
    private String values;
}
