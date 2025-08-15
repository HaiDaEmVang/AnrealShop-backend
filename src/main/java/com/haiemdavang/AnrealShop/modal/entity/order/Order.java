package com.haiemdavang.AnrealShop.modal.entity.order;


import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.OrderStatus; // Import nếu bạn thêm OrderStatus
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "shippingAddress", "payment", "orderItems", "shopOrders"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private UserAddress shippingAddress;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

     @Enumerated(EnumType.STRING)
     @Column(name = "status", nullable = false)
     @Builder.Default
     private OrderStatus status = OrderStatus.PROCESSING;

     @Column(name = "sub_total_amount")
     private Long subTotalAmount;

     @Column(name = "shipping_fee")
     private Long shippingFee;

     @Column(name = "grand_total_amount", nullable = false)
     private Long grandTotalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShopOrder> shopOrders = new HashSet<>();


    public void addOrderItem(OrderItem item) {
        if (orderItems == null) {
            orderItems = new HashSet<>();
        }
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        if (orderItems != null) {
            orderItems.remove(item);
            item.setOrder(null);
        }
    }
    public void addShopOrder(ShopOrder shopOrder) {
        if (shopOrders == null) {
            shopOrders = new HashSet<>();
        }
        shopOrders.add(shopOrder);
        shopOrder.setOrder(this);
    }

    public void removeShopOrder(ShopOrder shopOrder) {
        if (shopOrders != null) {
            shopOrders.remove(shopOrder);
            shopOrder.setOrder(null);
        }
    }
}