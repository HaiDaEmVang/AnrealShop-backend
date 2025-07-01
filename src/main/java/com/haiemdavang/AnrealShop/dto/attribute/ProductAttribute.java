package com.haiemdavang.AnrealShop.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    private String attributeKeyName;
    private String attributeKeyDisplay;
    private List<String> value;
}
