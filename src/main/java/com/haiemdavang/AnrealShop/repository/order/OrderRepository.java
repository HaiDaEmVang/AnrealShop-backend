package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("select od from Order od " +
            "left join fetch od.payment p " +
            "left join fetch od.orderItems " +
            "feft join fetch od.shopOrders " +
            "where od.id = :id")
    Optional<Order> findWithPaymentById(String id);


}
