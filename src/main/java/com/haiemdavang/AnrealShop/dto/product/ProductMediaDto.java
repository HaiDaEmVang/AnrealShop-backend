package com.haiemdavang.AnrealShop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMediaDto {
    private String id;
    private String url;
    private String thumbnailUrl;
    private String type;
}
