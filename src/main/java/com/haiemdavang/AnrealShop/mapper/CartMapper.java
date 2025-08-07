package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeValue;
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
                    .map(AttributeValue::getValue)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }

        return CartItemDto.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductSku() != null && cartItem.getProductSku().getProduct() != null ?
                        cartItem.getProductSku().getProduct().getId() : null)
                .productSkuId(cartItem.getProductSku() != null ? cartItem.getProductSku().getId() : null)
                .thumbnailUrl(cartItem.getProductSku().getThumbnailUrl())
                .quantity(cartItem.getQuantity())
                .maxQuantity(cartItem.getProductSku() != null ? cartItem.getProductSku().getQuantity() : 0)
                .price(cartItem.getPrice())
                .name(cartItem.getProductSku() != null && cartItem.getProductSku().getProduct() != null ?
                        cartItem.getProductSku().getProduct().getName() : null)
                .attributeString(attributeString)
                .build();
    }
}
