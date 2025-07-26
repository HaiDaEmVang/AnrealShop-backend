package com.haiemdavang.AnrealShop.modal.entity.product;


import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeValue;
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
})
public class ProductSku {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private Long price;

    @Builder.Default
    @Column(nullable = false)
    private int sold = 0;

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
            name = "sku_attributes",
            joinColumns = @JoinColumn(name = "sku_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private Set<AttributeValue> attributes;

    @Column(name = "image_urls")
    private String thumbnailUrl;
}