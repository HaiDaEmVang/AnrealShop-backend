package com.haiemdavang.AnrealShop.repository.product;

import com.haiemdavang.AnrealShop.dto.product.IProductStatus;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    @Query(
            value = """
                select restrict_status as id, count(id) as count from products
                where shop_id = :shopId
                group by restrict_status""",
            nativeQuery = true
    )
    List<IProductStatus> getMetaSumMyProductByStatus(String shopId);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @EntityGraph(attributePaths = {
            "category",
            "mediaList",
            "generalAttributes",
    })
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findWithCategoryAndMediaAndGeneralAttributeById(String id);
}
