package com.haiemdavang.AnrealShop.kafka.dto;

import com.haiemdavang.AnrealShop.dto.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSyncMessage {
    private String action; // "CREATED", "UPDATED", "DELETED"
    private ProductDetailDTO product;
}
