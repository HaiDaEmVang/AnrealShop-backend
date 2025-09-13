package com.haiemdavang.AnrealShop.repository.shipment;

import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipping, String> {
}
