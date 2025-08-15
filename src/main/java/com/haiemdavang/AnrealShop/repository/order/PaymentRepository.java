package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
