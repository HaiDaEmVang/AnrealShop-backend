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
    private String urlSlug;

    @Column(length = 100)
    private String urlPath;

    @Column
    private Integer level;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

    @Column(name = "order", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int order;

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

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parent=" + (parent != null ? parent.getId() : null) +
                ", description='" + description + '\'' +
                ", urlSlug='" + urlSlug + '\'' +
                ", urlPath='" + urlPath + '\'' +
                ", level=" + level +
                ", active=" + active +
                ", order=" + order +
                ", hasChildren=" + hasChildren +
                ", productCount=" + productCount +
                ", createdAt=" + createdAt +
                '}';
    }
}