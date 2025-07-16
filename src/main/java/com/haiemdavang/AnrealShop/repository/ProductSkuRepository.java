package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSkuRepository extends JpaRepository<ProductSku, String> {
    List<ProductSku> findByProductIdIn(List<String> productIds);

    @Query("SELECT ps FROM ProductSku ps LEFT JOIN FETCH ps.attributes WHERE ps.product.id = :id")
    List<ProductSku> findWithAttributeByProductId(String id);
}
