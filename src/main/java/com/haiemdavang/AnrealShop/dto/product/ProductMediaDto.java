package com.haiemdavang.AnrealShop.dto.product;

import com.haiemdavang.AnrealShop.modal.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMediaDto {
    private String url;
    private String type;
}
