package com.haiemdavang.AnrealShop.modal.entity.product;


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
@ToString(exclude = {"product", "attributes"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "product_skus", indexes = {
        @Index(name = "idx_productsku_sku_unique", columnList = "sku", unique = true)
        // Hoặc @Index(name = "idx_productsku_product_sku", columnList = "product_id, sku", unique = true)
})
public class ProductSku {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 50, unique = true)
    private String sku;

    @Column(nullable = false)
    private Long price; // Giả định giá là số nguyên (đơn vị nhỏ nhất)

    @Column(columnDefinition = "INT DEFAULT 0")
    private int quantity = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "sku_attributes", // Tên bảng nối trong CSDL
            joinColumns = @JoinColumn(name = "sku_id"), // Cột trong bảng nối trỏ về ProductSku
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id") // Trỏ về id của AttributeValue
    )
    private Set<AttributeValue> attributes;
}