package com.haiemdavang.AnrealShop.modal.entity.sku;

import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
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
                @UniqueConstraint(name = "uk_attrvalue_key_value_shop", columnNames  = "attribute_key_id, value, shop_id")
        }
)
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_key_id", nullable = false)
    private AttributeKey attributeKey; // Loại thuộc tính mà giá trị này thuộc về

    @Column(nullable = false, length = 255)
    private String value; // Ví dụ: "Đỏ", "XL", "Cotton"

    @Column(name = "display_order", columnDefinition = "INT DEFAULT 0")
    private int displayOrder = 0; // Thứ tự hiển thị (nếu cần)

    @Column(columnDefinition = "TEXT")
    private String metadata; // Dữ liệu bổ sung, ví dụ: mã màu HEX cho màu sắc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "attributes", fetch = FetchType.LAZY)
    private Set<ProductSku> productSkus;
}