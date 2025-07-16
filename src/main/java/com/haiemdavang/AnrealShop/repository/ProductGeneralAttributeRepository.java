package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeSingleValueDto;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductAttributeId;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductGeneralAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductGeneralAttributeRepository extends JpaRepository<ProductGeneralAttribute, ProductAttributeId> {

    @Query("SELECT new com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeSingleValueDto(" +
            "pga.attributeValue.attributeKey.keyName, " +
            "pga.attributeValue.attributeKey.displayName, " +
            "pga.attributeValue.value) " +
            "FROM ProductGeneralAttribute pga WHERE pga.product.id = ?1")
    List<ProductAttributeSingleValueDto> findProductAttributeByIdProduct(String id);

    @Query("SELECT pga FROM ProductGeneralAttribute pga LEFT JOIN FETCH pga.attributeValue WHERE pga.product.id = ?1")
    List<ProductGeneralAttribute> findProductGeneralAttributesByProductId(String id);
}
