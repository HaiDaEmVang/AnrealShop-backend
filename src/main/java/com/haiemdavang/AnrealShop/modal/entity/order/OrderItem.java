package com.haiemdavang.AnrealShop.modal.entity.order;


import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.CancelBy;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
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
@ToString(exclude = { "productSku", "order", "shopOrder", "shippings", "trackingHistory"})
@EqualsAndHashCode(of = {"id", "order", "productSku"})
@NamedEntityGraph(
        name = "OrderItem.graph.forShop",
        attributeNodes = {
                @NamedAttributeNode(value = "productSku", subgraph = "productSkuSubgraph"),
                @NamedAttributeNode("trackingHistory"),
                @NamedAttributeNode("shippings")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "productSkuSubgraph",
                        attributeNodes = {
                                @NamedAttributeNode("product"),
                                @NamedAttributeNode("attributes")
                        }
                )
        }
)
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sku_id", nullable = false)
    private ProductSku productSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id", nullable = false)
    private ShopOrder shopOrder;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean success = false;

    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceled_by")
    private CancelBy canceledBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private OrderTrackStatus status = OrderTrackStatus.PROCESSING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Shipping> shippings = new HashSet<>();

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OrderItemTrack> trackingHistory = new HashSet<>();

    public void addTrackingHistory(OrderItemTrack track) {
        if (trackingHistory == null) trackingHistory = new HashSet<>();
        trackingHistory.add(track);
        track.setOrderItem(this);
    }


    public void setStatus(OrderTrackStatus status) {
        this.status = status;
        OrderItemTrack shopOrderTrack = new OrderItemTrack(this, status, LocalDateTime.now());
        this.addTrackingHistory(shopOrderTrack);
    }


}