package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.cart.Cart;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.redis.service.IRedisService;
import com.haiemdavang.AnrealShop.repository.CartRepository;
import com.haiemdavang.AnrealShop.repository.product.ProductRepository;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements ICartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final IRedisService redisService;
    private final SecurityUtils securityUtils;
    private final String PREFIX_CART = "user:%s:cart";

    @Override
    public int countByUserId(String userId) {
        String key = String.format(PREFIX_CART, userId);
        Integer cachedCount = redisService.getValue(key, -1);
        if (cachedCount != -1) return cachedCount;
        int count = cartRepository.countByUserId(userId);
        redisService.addValue(key, count);
        return count;
    }
//
//    @Override
//    public void addToCart(CartItemDto cartItemDto) {
//        User currentUser = securityUtils.getCurrentUser();
//        String userId = currentUser.getId();
//
//        Cart cart = findOrCreateCart(currentUser);
//        Product product = productRepository.findById(cartItemDto.getProductId())
//                .orElseThrow(() -> new BadRequestException("PRODUCT_NOT_FOUND"));
//
//        Optional<CartItem> existingItem = cart.getItems().stream()
//                .filter(item -> item.getProduct().getId().equals(cartItemDto.getProductId()))
//                .findFirst();
//
//        if (existingItem.isPresent()) {
//            CartItem item = existingItem.get();
//            item.setQuantity(item.getQuantity() + cartItemDto.getQuantity());
//        } else {
//            CartItem newItem = CartItem.builder()
//                    .cart(cart)
//                    .product(product)
//                    .quantity(cartItemDto.getQuantity())
//                    .price(product.getPrice())
//                    .selected(true)
//                    .build();
//            cart.addItem(newItem);
//        }
//
//        cartRepository.save(cart);
//        invalidateCartCache(userId);
//    }
//
//    @Override
//    public void removeFromCart(String productId) {
//        User currentUser = securityUtils.getCurrentUser();
//        String userId = currentUser.getId();
//
//        Cart cart = findOrCreateCart(currentUser);
//        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
//
//        cartRepository.save(cart);
//        invalidateCartCache(userId);
//    }
//
//    @Override
//    public void clearCart(List<String> productIds) {
//        User currentUser = securityUtils.getCurrentUser();
//        String userId = currentUser.getId();
//
//        Cart cart = findOrCreateCart(currentUser);
//        cart.getItems().removeIf(item -> productIds.contains(item.getProduct().getId()));
//
//        cartRepository.save(cart);
//        invalidateCartCache(userId);
//    }
//
//    @Override
//    public List<CartItemDto> getCartItems() {
//        User currentUser = securityUtils.getCurrentUser();
//        Cart cart = findOrCreateCart(currentUser);
//
//        return cart.getItems().stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
//
//    private Cart findOrCreateCart(User user) {
//        return cartRepository.findByUserId(user.getId())
//                .orElseGet(() -> {
//                    Cart newCart = Cart.builder()
//                            .user(user)
//                            .build();
//                    return cartRepository.save(newCart);
//                });
//    }

//    private CartItemDto convertToDto(CartItem cartItem) {
//        return CartItemDto.builder()
//                .id(cartItem.getId())
//                .productId(cartItem.getProduct().getId())
//                .quantity(cartItem.getQuantity())
//                .price(cartItem.getPrice())
//                .selected(cartItem.isSelected())
//                .build();
//    }

//    private void invalidateCartCache(String userId) {
//        String key = String.format(PREFIX_CART, userId);
//        redisService.deleteValue(key);
//    }
}