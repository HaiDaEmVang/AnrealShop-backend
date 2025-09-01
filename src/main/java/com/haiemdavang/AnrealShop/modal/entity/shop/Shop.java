package com.haiemdavang.AnrealShop.modal.entity.shop;


import com.haiemdavang.AnrealShop.modal.entity.category.ShopCategory;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "shopCategories"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "shops")
@SQLDelete(sql = "UPDATE shops SET deleted = true, updated_at = CURRENT_TIMESTAMP WHERE id = ?") // Khi gọi delete(), Hibernate sẽ chạy lệnh này
@Where(clause = "deleted = false")
public class Shop {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String urlSlug;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "product_count", columnDefinition = "INT DEFAULT 0")
    private int productCount;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private long revenue;

    @Column(name = "average_rating", columnDefinition = "FLOAT DEFAULT 0")
    private float averageRating;

    @Column(name = "total_reviews", columnDefinition = "INT DEFAULT 0")
    private int totalReviews;

    @Column(name = "follower_count", columnDefinition = "INT DEFAULT 0")
    private int followerCount;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted = false;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ShopCategory> shopCategories;

     @UpdateTimestamp
     @Column(name = "updated_at")
     private LocalDateTime updatedAt;
}