package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.cart.CartDto;
import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.mapper.CartMapper;
import com.haiemdavang.AnrealShop.mapper.ShopMapper;
import com.haiemdavang.AnrealShop.modal.entity.cart.Cart;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.redis.service.IRedisService;
import com.haiemdavang.AnrealShop.repository.CartItemRepository;
import com.haiemdavang.AnrealShop.repository.CartRepository;
import com.haiemdavang.AnrealShop.repository.ProductSkuRepository;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductSkuRepository productSkuRepository;
    private final IRedisService redisService;
    private final SecurityUtils securityUtils;
    private final CartMapper cartMapper;
    private final ShopMapper shopMapper;
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

    @Override
    public void addToCart(CartItemDto cartItemDto) {
//        check neu la sp cua chinh user thi throw forbiden
        User currentUser = securityUtils.getCurrentUser();
        String userId = currentUser.getId();

        Cart cart = findOrCreateCart(currentUser);
        ProductSku productSku = productSkuRepository.findById(cartItemDto.getProductSkuId())
                .orElseThrow(() -> new BadRequestException("PRODUCT_SKU_NOT_FOUND"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductSku().getId().equals(cartItemDto.getProductSkuId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setPrice(item.getPrice());
            item.setQuantity(item.getQuantity() + cartItemDto.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productSku(productSku)
                    .quantity(cartItemDto.getQuantity())
                    .price(productSku.getPrice())
                    .selected(true)
                    .build();
            cart.addItem(newItem);
        }

        cartRepository.save(cart);
        invalidateCartCache(userId);
    }

    @Override
    public void removeFromCart(String cartItemId) {
        User currentUser = securityUtils.getCurrentUser();
        String userId = currentUser.getId();

        Cart cart = findOrCreateCart(currentUser);
        if (cart.getItems().stream().anyMatch(item -> item.getId().equals(cartItemId)))
            throw new BadRequestException("CART_ITEM_NOT_FOUND");
        else
            cart.getItems().removeIf(item -> item.getId().equals(cartItemId));

        cartRepository.save(cart);
        invalidateCartCache(userId);
    }

    @Override
    public void clearCart(Set<String> productIds) {
        User currentUser = securityUtils.getCurrentUser();
        String userId = currentUser.getId();

        Cart cart = findOrCreateCart(currentUser);
        cart.getItems().removeIf(item -> productIds.contains(item.getId()));

        cartRepository.save(cart);
        invalidateCartCache(userId);
    }

    @Override
    public Set<CartDto> getCartItems() {
        User currentUser = securityUtils.getCurrentUser();
        Set<CartItem> cartItem = cartItemRepository.findCartItemsByUserId(currentUser.getId());

        Map<Shop, Set<CartItem>> cartMap = cartItem.stream().collect(
                Collectors.groupingBy(
                        item -> item.getProductSku().getProduct().getShop(),
                        Collectors.toSet()
                )
        );
        Set<CartDto> cartsDto = new HashSet<>();

        cartMap.forEach((s, set) -> {
            CartDto cart = CartDto.builder()
                    .shop(shopMapper.toBaseShopDto(s))
                    .items(set.stream().map(cartMapper::toCartItemDto).collect(Collectors.toSet()))
                    .build();
            cartsDto.add(cart);
        });



        return cartsDto;
    }

    private Cart findOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private void invalidateCartCache(String userId) {
        String key = String.format(PREFIX_CART, userId);
        redisService.del(key);
    }
}