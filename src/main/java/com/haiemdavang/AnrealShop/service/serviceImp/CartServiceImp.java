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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    @Transactional
    public boolean addToCart(CartItemDto cartItemDto) {
//        check neu la sp cua chinh user thi throw forbiden
        User currentUser = securityUtils.getCurrentUser();
        String userId = currentUser.getId();

        Cart cart = findOrCreateCart(currentUser);
        ProductSku productSku = productSkuRepository.findById(cartItemDto.getProductSkuId())
                .orElseThrow(() -> new BadRequestException("PRODUCT_SKU_NOT_FOUND"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductSku().getId().equals(cartItemDto.getProductSkuId()))
                .findFirst();

        boolean isNew = existingItem.isEmpty();
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
        return isNew;
    }

    @Override
    @Transactional
    public void removeFromCart(String cartItemId) {

        User currentUser = securityUtils.getCurrentUser();
        String userId = currentUser.getId();

        Cart cart = findOrCreateCart(currentUser);
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        if (!removed) {
            throw new BadRequestException("CART_ITEM_NOT_FOUND");
        }

        cartRepository.save(cart);
        invalidateCartCache(userId);
    }

    @Override
    @Transactional
    public int clearCart(List<String> productIds) {
        User currentUser = securityUtils.getCurrentUser();
        String userId = currentUser.getId();

        Cart cart = findOrCreateCart(currentUser);
        Set<CartItem> itemsToRemove = cart.getItems().stream()
                .filter(item -> productIds.contains(item.getId()))
                .collect(Collectors.toSet());

        cart.getItems().removeAll(itemsToRemove);

        cartRepository.save(cart);
        invalidateCartCache(userId);
        return itemsToRemove.size();
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

    @Override
    public Map<Shop, Set<CartItem>> getCartItemsByIdIn(List<String> cartItemIds) {
        Set<CartItem> cartItems = cartItemRepository.findAllByIdIn(cartItemIds);

        return cartItems.stream().collect(
                Collectors.groupingBy(
                        item -> item.getProductSku().getProduct().getShop(),
                        Collectors.toSet()
                )
        );
    }

    @Override
    @Transactional
    public void updateQuantity(String cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findWithProductSkuById(cartItemId)
                .orElseThrow(() -> new BadRequestException("CART_ITEM_NOT_FOUND"));

        if (quantity > cartItem.getProductSku().getQuantity())
            throw new BadRequestException("QUANTITY_EXCEED_PRODUCT_SKU_STOCK");

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);
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