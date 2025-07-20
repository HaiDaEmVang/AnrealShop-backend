package com.haiemdavang.AnrealShop.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseCategoryDto {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
     
    @JsonProperty("urlPath")
    private String urlPath;

    @JsonProperty("urlSlug")
    private String urlSlug;
}
