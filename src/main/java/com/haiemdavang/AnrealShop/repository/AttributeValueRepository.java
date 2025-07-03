package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, String> {
    List<AttributeValue> findByAttributeKeyAndValueIn(AttributeKey ak, List<String> valuesForThisKey);
    List<AttributeValue> findByAttributeKey(AttributeKey attributeKey);
    List<AttributeValue> findByAttributeKeyIn(List<AttributeKey> attributeKeys);
}
