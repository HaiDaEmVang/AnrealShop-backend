package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.dto.order.IMetaDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@NonNullApi
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, String>, JpaSpecificationExecutor<ShopOrder> {
    @Query(value = "SELECT so.status as status, sum(so.totalAmount) as totalAmount , count(oi.id) as count " +
            "FROM ShopOrder so " +
            "left join so.orderItems oi " +
            "where so.shop.id = :id " +
            "AND so.createdAt BETWEEN :fromDate AND :toDate " +
            "group by so.status")
    Set<IMetaDto> countOrderItemByStatus(String id, LocalDateTime toDate, LocalDateTime fromDate);

    @EntityGraph(attributePaths = {
            "order",
            "order.payment",
            "user",
    })
    Page<ShopOrder> findAll(@Nullable Specification<ShopOrder> orderSpecification, Pageable pageable);


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
