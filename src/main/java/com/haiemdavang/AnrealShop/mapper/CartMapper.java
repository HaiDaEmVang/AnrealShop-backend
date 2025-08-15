package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeValue;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
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
                .urlSlug(cartItem.getProductSku() != null && cartItem.getProductSku().getProduct() != null ?
                        cartItem.getProductSku().getProduct().getUrlSlug() : null)
                .productId(cartItem.getProductSku() != null && cartItem.getProductSku().getProduct() != null ?
                        cartItem.getProductSku().getProduct().getId() : null)
                .productSkuId(cartItem.getProductSku() != null ? cartItem.getProductSku().getId() : null)
                .thumbnailUrl(cartItem.getProductSku().getThumbnailUrl())
                .quantity(cartItem.getQuantity())
                .maxQuantity(cartItem.getProductSku() != null ? cartItem.getProductSku().getQuantity() : 0)
                .price(cartItem.getPrice())
                .isSelected(cartItem.isSelected())
                .name(cartItem.getProductSku() != null && cartItem.getProductSku().getProduct() != null ?
                        cartItem.getProductSku().getProduct().getName() : null)
                .attributeString(attributeString)
                .build();
    }

    public CartItemDto toCartItemDto(ProductSku productSku, Integer newQuantity) {
        if (productSku == null) {
            return null;
        }

        String attributeString = "";
        if (productSku.getAttributes() != null) {
            attributeString = productSku.getAttributes().stream()
                    .map(AttributeValue::getValue)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }

        return CartItemDto.builder()
                .id(productSku.getId())
                .urlSlug(productSku.getProduct() != null ?
                        productSku.getProduct().getUrlSlug() : null)
                .productId(productSku.getProduct() != null ?
                        productSku.getProduct().getId() : null)
                .productSkuId(productSku.getId())
                .thumbnailUrl(productSku.getThumbnailUrl())
                .quantity(newQuantity)
                .maxQuantity(productSku.getQuantity())
                .price(productSku.getPrice())
                .isSelected(true)
                .name(productSku.getProduct() != null ?
                        productSku.getProduct().getName() : null)
                .attributeString(attributeString)
                .build();
    }
}
