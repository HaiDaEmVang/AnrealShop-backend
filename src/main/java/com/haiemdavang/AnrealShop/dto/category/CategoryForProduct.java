package com.haiemdavang.AnrealShop.dto.category;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryForProduct extends BaseCategoryDto{
    private String parentId;
    private int level;
    private boolean hasChildren;
}
