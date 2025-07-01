package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeKeyRepository extends JpaRepository<AttributeKey, String> {
    List<AttributeKey> findByDisplayNameIn(List<String> displayNames);
    List<AttributeKey> findAllByIsDefaultTrueOrShopsContains(Shop shop);
}
