package com.haiemdavang.AnrealShop.repository.product;

import com.haiemdavang.AnrealShop.dto.product.IProductStatus;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    @Query(
            value = """
                select restrict_status as id, count(id) as count from products
                where shop_id = :shopId and deleted = false
                group by restrict_status""",
            nativeQuery = true
    )
    Set<IProductStatus> getMetaSumMyProductByStatus(String shopId);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @EntityGraph(attributePaths = {
            "category",
            "mediaList",
            "generalAttributes",
            "productSkus"
    })
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findWithCategoryAndMediaAndGeneralAttributeById(String id);

    @Modifying
    @Query("UPDATE Product p SET p.deleted = true, p.visible = false WHERE p.id = :id")
    void softDelById(String id);
    @Modifying
    @Query("UPDATE Product p SET p.deleted = true, p.visible = false WHERE p.id in :ids")
    void softDelById(Collection<String> ids);

    Set<Product> findByIdIn(Collection<String> ids);

    void deleteByIdIn(Collection<String> ids);

    @EntityGraph(attributePaths = {
            "category",
            "shop",
            "mediaList",
    })
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product findBaseInfoById(String id);
}
