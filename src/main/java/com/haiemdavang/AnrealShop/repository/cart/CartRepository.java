package com.haiemdavang.AnrealShop.repository.cart;

import com.haiemdavang.AnrealShop.modal.entity.cart.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    int countByUserId(String user_id);

    @EntityGraph(attributePaths = "items")
    Optional<Cart> findByUserId(String id);

}
