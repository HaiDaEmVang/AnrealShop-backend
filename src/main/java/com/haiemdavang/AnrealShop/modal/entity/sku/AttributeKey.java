package com.haiemdavang.AnrealShop.modal.entity.sku;

import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.enums.*;
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
@ToString(exclude = {"shop", "attributeValues"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "attribute_keys", indexes = {
        @Index(name = "idx_attribute_key_name", columnList = "key_name", unique = true)
})
public class AttributeKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "key_name", nullable = false, length = 50, unique = true) // unique = true cho key_name
    private AttributeKeyType keyName;
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @PrePersist
    protected void onPrePersist() {
        if (this.keyName != null && this.displayName == null) {
            this.displayName = this.keyName.getDisplayName(); // Gán giá trị mặc định từ Enum
        }
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id") // Nullable
    private Shop shop;

    @Column(name = "allow_custom_values", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean allowCustomValues = true; // Shop có được thêm giá trị tùy chỉnh cho key này không?

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Danh sách các giá trị có thể có cho loại thuộc tính này.
     */
    @OneToMany(mappedBy = "attributeKey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AttributeValue> attributeValues;
}