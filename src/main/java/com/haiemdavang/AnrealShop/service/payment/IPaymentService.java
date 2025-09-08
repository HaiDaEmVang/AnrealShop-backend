package com.haiemdavang.AnrealShop.service.payment;

import com.haiemdavang.AnrealShop.modal.entity.order.Payment;
import com.haiemdavang.AnrealShop.modal.enums.PaymentGateway;
import com.haiemdavang.AnrealShop.modal.enums.PaymentStatus;
import com.haiemdavang.AnrealShop.modal.enums.PaymentType;

public interface IPaymentService {
    Payment createPayment(long grandTotalAmount, PaymentGateway paymentGateway, PaymentType bankTransfer);

    void updatePayment(Payment payment, PaymentStatus completed);
}
