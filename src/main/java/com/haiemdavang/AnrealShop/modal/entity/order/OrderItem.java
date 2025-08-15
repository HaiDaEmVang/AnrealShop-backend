package com.haiemdavang.AnrealShop.modal.entity.order;


import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
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
@ToString(exclude = {"order", "productSku"})
@EqualsAndHashCode(of = {"id", "order", "productSku"})
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

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean success = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // @ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
    // private Set<ShopOrder> shopOrders;
}