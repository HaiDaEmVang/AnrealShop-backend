package com.haiemdavang.AnrealShop.modal.entity.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String urlKey;

    @Column(length = 100)
    private String urlPath;

    @Column(name = "has_children", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean hasChildren;

    @Column(name = "product_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int productCount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<ShopCategoryItem> shopCategoryItems;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<DisplayCategory> displayCategories;
}
