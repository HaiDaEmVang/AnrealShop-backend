package com.haiemdavang.AnrealShop.repository.shipping;

import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NonNullApi
@Repository
public interface ShipmentRepository extends JpaRepository<Shipping, String> {

//    @EntityGraph(attributePaths = {
//            "trackingHistory",
//            "shopOrder",
//            "shopOrder.orderItems",
//            "shopOrder.orderItems.productSku",
//    })
    Page<Shipping> findAll(Specification<Shipping> shipSpecification, Pageable pageable);

    @EntityGraph(attributePaths = {
            "trackingHistory",
    })
    Shipping findByShopOrderId(String shopOrderId);

    @Override
    @EntityGraph(attributePaths = {"shopOrder", "trackingHistory"})
    Optional<Shipping> findById(String s);

    List<Shipping> findByShopOrderIdIn(Collection<String> shopOrderIds);

    List<Shipping> findAllByShopOrderStatus(ShopOrderStatus shopOrderStatus);
}
