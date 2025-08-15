package com.haiemdavang.AnrealShop.service.payment;

import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.order.Payment;
import com.haiemdavang.AnrealShop.modal.enums.PaymentGateway;
import com.haiemdavang.AnrealShop.modal.enums.PaymentType;
import com.haiemdavang.AnrealShop.repository.order.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment createPayment(long grandTotalAmount, PaymentGateway paymentGateway, PaymentType bankTransfer) {
        LocalDateTime timeExpiration = LocalDateTime.now().plusMinutes(30);
        Payment payment = Payment.builder()
                .expireAt(timeExpiration)
                .type(bankTransfer)
                .gateway(paymentGateway)
                .amount(grandTotalAmount)
        .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePayment(Payment payment) {
        if (paymentRepository.existsById(payment.getId())) {
            paymentRepository.save(payment);
        }else {
            throw new BadRequestException("PAYMENT_NOT_FOUND");
        }
    }
}
