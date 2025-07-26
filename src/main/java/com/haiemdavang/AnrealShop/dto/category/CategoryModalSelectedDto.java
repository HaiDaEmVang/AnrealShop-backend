package com.haiemdavang.AnrealShop.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryModalSelectedDto extends BaseCategoryDto{

    @JsonProperty("parentId")
    private String parentId;

    @JsonProperty("hasChildren")
    private boolean hasChildren;

    private int level;
}
