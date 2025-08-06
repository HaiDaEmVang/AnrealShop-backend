    package com.haiemdavang.AnrealShop.modal.entity.product;

    import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeValue;
    import com.haiemdavang.AnrealShop.modal.entity.category.Category;
    import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
    import com.haiemdavang.AnrealShop.modal.enums.RestrictStatus;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.SQLDelete;
    import org.hibernate.annotations.UpdateTimestamp;
    import org.hibernate.annotations.Where;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    @Entity
    @Table(name = "products")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(of = "id")
    @Where(clause = "deleted = false")
    public class Product {
    
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(length = 36, updatable = false, nullable = false)
        private String id;
    
        @Column(nullable = false, length = 255)
        private String name;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "shop_id", nullable = false)
        private Shop shop;
    
        @Column(columnDefinition = "TEXT")
        private String sortDescription;
    
        @Column(columnDefinition = "TEXT")
        private String description;
    
        @Column(columnDefinition = "TEXT")
        private String urlSlug;
    
        @Builder.Default
        @Column(name = "thumbnail_url", length = 255)
        private String thumbnailUrl = "https://res.cloudinary.com/dlcjc36ow/image/upload/v1747916255/ImagError_jsv7hr.png";
    
        @Column(nullable = false)
        private Long price;

        @Column(nullable = false)
        private Long discountPrice;
    
        @Column(nullable = false)
        private int quantity = 0;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private Category category;
    
        @Column(nullable = false, columnDefinition = "DECIMAL(10,2) default 0.00")
        private BigDecimal weight;
        @Column(nullable = false, columnDefinition = "DECIMAL(10,2) default 0.00")
        private BigDecimal height;
        @Column(nullable = false, columnDefinition = "DECIMAL(10,2) default 0.00")
        private BigDecimal length;
        @Column(nullable = false, columnDefinition = "DECIMAL(10,2) default 0.00")
        private BigDecimal width;
    
        @Column(nullable = false)
        private long revenue = 0;
    
        @Builder.Default
        @Column(nullable = false)
        private int sold = 0;
    
        @Column(name = "average_rating", nullable = false)
        private float averageRating = 0;
    
        @Column(name = "total_reviews", nullable = false)
        private int totalReviews = 0;
    
        @Builder.Default
        @Column(nullable = false)
        private boolean visible = true;
    
        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;
    
        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    
        @Column(nullable = false)
        private boolean restricted = false;
    
        @Column(name = "restricted_reason", columnDefinition = "TEXT")
        private String restrictedReason;
    
        @Builder.Default
        @Enumerated(EnumType.STRING)
        @Column(name = "restrict_status", nullable = false)
        private RestrictStatus restrictStatus = RestrictStatus.PENDING;
    
        @Builder.Default
        @Column(nullable = false)
        private boolean deleted = false;
    
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
        private Set<ProductMedia> mediaList;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private Set<ProductGeneralAttribute> generalAttributes = new HashSet<>();

        public void addGeneralAttribute(AttributeValue attributeValue) {
            ProductGeneralAttribute productAttribute = new ProductGeneralAttribute();
            productAttribute.setProduct(this);
            productAttribute.setAttributeValue(attributeValue);
            productAttribute.setId(new ProductAttributeId(this.id, attributeValue.getId()));
            if(this.generalAttributes == null) {
                this.generalAttributes = new HashSet<>();
            }
            this.generalAttributes.add(productAttribute);
        }

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private Set<ProductSku> productSkus;

        public void addMedia(ProductMedia productMedia) {
            if (this.mediaList == null) {
                this.mediaList = new HashSet<>();
            }
            this.mediaList.add(productMedia);
            productMedia.setProduct(this);
        }

    }
