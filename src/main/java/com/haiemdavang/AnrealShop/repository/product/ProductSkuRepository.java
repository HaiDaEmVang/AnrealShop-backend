package com.haiemdavang.AnrealShop.repository.product;

import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductSkuRepository extends JpaRepository<ProductSku, String> {
    @Query("SELECT ps From ProductSku ps " +
            "LEFT JOIN FETCH ps.product p " +
            "LEFT JOIN FETCH p.shop s " +
            "WHERE ps.id in :productSkuIds")
    List<ProductSku> findByProductIdIn(Collection<String> productSkuIds);

    @Query("SELECT ps FROM ProductSku ps LEFT JOIN FETCH ps.attributes WHERE ps.product.id = :id")
    List<ProductSku> findWithAttributeByProductId(String id);
}
