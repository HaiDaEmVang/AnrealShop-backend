package com.haiemdavang.AnrealShop.modal.entity.shop;


import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "order", "shop", "trackingHistory", "shippingAddress"}) // Đổi tên items thành orderItems cho rõ
@EqualsAndHashCode(of = {"id", "user", "order", "shop"})
@Entity
@Table(name = "shop_orders")
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người dùng đặt đơn hàng chính

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Đơn hàng chính (parent order)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private ShopAddress shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "shipping_fee", nullable = false)
    @Builder.Default
    private int shippingFee = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private OrderTrackStatus status = OrderTrackStatus.PROCESSING;

    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceled_by")
    private CancelBy canceledBy;

    @Column(nullable = false, name = "total_amount")
    private Long totalAmount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShopOrderTrack> trackingHistory = new HashSet<>();

    public void addTrackingHistory(ShopOrderTrack track) {
        if (trackingHistory == null) trackingHistory = new HashSet<>();
        trackingHistory.add(track);
        track.setShopOrder(this);
    }

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OrderItem> orderItems = new HashSet<>();

    public void addOrderItems(OrderItem orderItem) {
        if (orderItems == null) orderItems = new HashSet<>();
        orderItems.add(orderItem);
        orderItem.setShopOrder(this);
    }

}
