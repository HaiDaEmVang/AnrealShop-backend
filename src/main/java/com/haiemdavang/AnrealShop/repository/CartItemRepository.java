package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    @Query("SELECT DISTINCT ci FROM CartItem ci " +
            "JOIN FETCH ci.cart c " +
            "JOIN FETCH c.user u " +
            "JOIN FETCH ci.productSku ps " +
            "JOIN FETCH ps.product p " +
            "JOIN FETCH p.shop s " +
            "JOIN FETCH ps.attributes at " +
            "WHERE u.id = :userId")
    Set<CartItem> findCartItemsByUserId(String userId);
}
