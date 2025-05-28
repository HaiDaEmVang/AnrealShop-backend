package com.haiemdavang.AnrealShop.modal.entity.order;


import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"order", "product", "selectedAttributes"})
@EqualsAndHashCode(of = "id")
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
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long price; // Giá mỗi đơn vị tại thời điểm đặt hàng (BIGINT trong SQL)

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean success = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_item_attributes",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private Set<AttributeValue> selectedAttributes;


    // @ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
    // private Set<ShopOrder> shopOrders;
}