package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@NonNullApi
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, String>, JpaSpecificationExecutor<ShopOrder> {

    @EntityGraph(value = "ShopOrder.graph.forShop", type = EntityGraph.EntityGraphType.FETCH)
    Page<ShopOrder> findAll(@Nullable Specification<ShopOrder> orderSpecification, Pageable pageable);

    @EntityGraph(attributePaths = {
            "user",
            "orderItems",
            "orderItems.productSku"
    })
    List<ShopOrder> findAll(@Nullable Specification<ShopOrder> orderSpecification);


    @Query(value = "SELECT so FROM ShopOrder so " +
            "LEFT JOIN FETCH so.orderItems oi " +
            "LEFT JOIN FETCH oi.productSku sku " +
            "LEFT JOIN FETCH sku.product p " +
            "LEFT JOIN FETCH so.order o " +
            "LEFT JOIN FETCH o.payment pay " +
            "LEFT JOIN FETCH so.trackingHistory " +
            "WHERE so.id = :shopOrderId")
    ShopOrder findWithFullInfoById(String shopOrderId);

    @Query(value = "SELECT so FROM ShopOrder so " +
            "LEFT JOIN FETCH so.orderItems oi " +
            "WHERE so.id = :shopOrderId")
    ShopOrder findWithOrderItemById(String shopOrderId);


}
