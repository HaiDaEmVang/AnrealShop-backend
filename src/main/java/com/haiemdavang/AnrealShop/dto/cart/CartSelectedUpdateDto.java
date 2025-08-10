package com.haiemdavang.AnrealShop.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartSelectedUpdateDto {
    private List<String> itemIds;
    private boolean selected;
}
