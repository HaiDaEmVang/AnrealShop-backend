package com.haiemdavang.AnrealShop.dto.attribute;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto extends ProductAttribute{
    private int displayOrder;
    private boolean isDefault;
    private boolean isMultiSelect;
}
