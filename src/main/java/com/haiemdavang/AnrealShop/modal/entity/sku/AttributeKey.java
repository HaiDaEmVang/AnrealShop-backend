package com.haiemdavang.AnrealShop.modal.entity.sku;

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

    @Column(name = "slug_name", nullable = false, length = 50)
    private String slugName; //tam thoi chua dung den nghe cu Hải

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "is_default", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "attributeKey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AttributeValue> attributeValues;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "shop_attribute_keys",
            joinColumns = @JoinColumn(name = "attribute_key_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id")
    )
    private Set<Shop> shops;
}