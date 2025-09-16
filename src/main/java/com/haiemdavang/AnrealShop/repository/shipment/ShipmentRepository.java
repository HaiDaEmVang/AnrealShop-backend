package com.haiemdavang.AnrealShop.repository.shipment;

import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipping, String> {

    @EntityGraph(attributePaths = {
            "trackingHistory",
            "orderItems",
            "orderItems.shopOrder",
            "orderItems.shopOrder.user",
            "orderItems.productSku"
    })
    Page<Shipping> findAll(Specification<Shipping> shipSpecification, Pageable pageable);
}
