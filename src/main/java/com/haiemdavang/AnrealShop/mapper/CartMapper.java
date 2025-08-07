package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.modal.entity.cart.Cart;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import org.springframework.stereotype.Service;

@Service
public class CartMapper {

    public CartItemDto toCartItemDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        String attributeString = "";
        if (cartItem.getProductSku() != null && cartItem.getProductSku().getAttributes() != null) {
            attributeString = cartItem.getProductSku().getAttributes().stream()
                    .map(attr -> attr.getValue())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }

        return CartItemDto.builder()
                .id(cartItem.getId())
                .productSkuId(cartItem.getProductSku() != null ? cartItem.getProductSku().getId() : null)
                .thumbnailUrl(cartItem.getProductSku().getThumbnailUrl())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .name(cartItem.getProductSku() != null && cartItem.getProductSku().getProduct() != null ?
                        cartItem.getProductSku().getProduct().getName() : null)
                .attributeString(attributeString)
                .build();
    }
}
