package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@NonNullApi
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    @EntityGraph(attributePaths = {
            "productSku",
            "productSku.product",
            "productSku.attributes",
            "trackingHistory",
    })
    List<OrderItem> findAll(Specification<OrderItem> spec);

    @EntityGraph(attributePaths = {
            "productSku",
            "productSku.product",
            "productSku.attributes",
    })
    List<OrderItem> findByIdIn(Collection<String> ids);
}
