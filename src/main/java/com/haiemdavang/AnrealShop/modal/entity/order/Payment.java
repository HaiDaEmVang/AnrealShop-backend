package com.haiemdavang.AnrealShop.modal.entity.order;

// Import các Enums và User nếu cần liên kết trực tiếp (ví dụ: người thực hiện thanh toán)
import com.haiemdavang.AnrealShop.modal.enums.PaymentGateway;
import com.haiemdavang.AnrealShop.modal.enums.PaymentStatus;
import com.haiemdavang.AnrealShop.modal.enums.PaymentType; // Nếu dùng Enum cho type
import com.haiemdavang.AnrealShop.modal.entity.order.Order; // Nếu có quan hệ ngược lại

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('VNPAY', 'CASH_ON_DELIVERY')")
    private PaymentGateway gateway;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PaymentType type;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('PENDING', 'COD', 'COMPLETED', 'EXPIRED', 'CANCELLED', 'REFUNDED', 'FAILED') DEFAULT 'PENDING'")
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    // private Set<Order> orders;
}