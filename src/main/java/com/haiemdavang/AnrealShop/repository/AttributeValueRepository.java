package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, String> {
    List<AttributeValue> findByAttributeKeyAndValueIn(AttributeKey ak, List<String> valuesForThisKey);
    List<AttributeValue> findByAttributeKeyIn(List<AttributeKey> attributeKeys);
    @Query("SELECT av FROM AttributeValue av " +
            "JOIN FETCH av.attributeKey ak " +
            "WHERE ak.keyName IN :keyNames AND av.value IN :values")
    Set<AttributeValue> findByAttributeKeyKeyNameInAndValueIn(Set<String> keyNames, Set<String> values);
}
