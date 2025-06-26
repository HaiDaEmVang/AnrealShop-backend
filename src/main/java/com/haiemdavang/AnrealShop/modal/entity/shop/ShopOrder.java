package com.haiemdavang.AnrealShop.modal.entity.shop;


import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "order", "shop", "trackingHistory", "orderItems"}) // Đổi tên items thành orderItems cho rõ
@EqualsAndHashCode(of = "id")
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

    @Column(name = "shipping_fee", columnDefinition = "INT DEFAULT 0")
    private int shippingFee = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceled_by")
    private CancelBy canceledBy;

    @Column(nullable = false)
    private Long total;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ShopOrderTrack> trackingHistory = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "shop_order_items",
            joinColumns = @JoinColumn(name = "shop_order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id")
    )
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>(); // Đổi tên từ items để rõ ràng hơn

    // Helper methods for collections (Tùy chọn)
    public void addTrackingHistory(ShopOrderTrack track) {
        if (trackingHistory == null) trackingHistory = new ArrayList<>();
        trackingHistory.add(track);
        track.setShopOrder(this);
    }

    public void addOrderItem(OrderItem item) {
        if (orderItems == null) orderItems = new ArrayList<>();
        orderItems.add(item);
        // Nếu OrderItem có danh sách Set<ShopOrder> shopOrders (mappedBy="orderItems")
        // thì cần item.getShopOrders().add(this);
    }
}
