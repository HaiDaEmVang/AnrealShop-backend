package com.haiemdavang.AnrealShop.modal.entity.sku;

import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
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
@ToString(exclude = {"attributeKey", "shop", "productSkus"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "attribute_values",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_attrvalue_key_value", columnNames  = "attribute_key_id, value")
        }
)
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_key_id", nullable = false)
    private AttributeKey attributeKey;

    @Column(nullable = false, length = 255)
    private String value;

    @Column(name = "display_order", columnDefinition = "INT DEFAULT 0")
    private int displayOrder = 0;

    @Column(name = "is_Default", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDefault = false;

    @Column(columnDefinition = "TEXT")
    private String metadata; // Tam thoi bo qua, co the dung den sau nha cac pro

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "attributes", fetch = FetchType.LAZY)
    private Set<ProductSku> productSkus;

}