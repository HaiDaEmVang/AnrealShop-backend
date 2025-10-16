package com.haiemdavang.AnrealShop.modal.entity.shop;


import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "order", "shop", "trackingHistory", "shippingAddress", "shipping"})
@EqualsAndHashCode(of = {"id", "user", "order", "shop"})
@NamedEntityGraph(
        name = "ShopOrder.graph.forShop",
        attributeNodes = {
                @NamedAttributeNode(value = "order", subgraph = "orderSubgraph"),
                @NamedAttributeNode("user"),
                @NamedAttributeNode("shop")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "orderSubgraph",
                        attributeNodes = @NamedAttributeNode("payment")
                )
        }
)
@Entity
@Table(name = "shop_orders")
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private ShopAddress shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "shipping_fee", nullable = false)
    @Builder.Default
    private Long shippingFee = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private ShopOrderStatus status = ShopOrderStatus.INIT_PROCESSING;


    @Column(nullable = false, name = "total_amount")
    private Long totalAmount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShopOrderTrack> trackingHistory = new HashSet<>();

    @OneToOne(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Shipping shipping;

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

    public void setStatus(ShopOrderStatus status) {
        this.status = status;
        ShopOrderTrack shopOrderTrack = new ShopOrderTrack(this, status, LocalDateTime.now());
        this.addTrackingHistory(shopOrderTrack);
    }

}
