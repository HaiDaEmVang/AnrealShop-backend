package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeKeyRepository extends JpaRepository<AttributeKey, String> {
    List<AttributeKey> findAllByIsDefaultTrueOrShopsContains(Shop shop);

    List<AttributeKey> findByKeyNameIn(List<String> list);
}
