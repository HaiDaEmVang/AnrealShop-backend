package com.haiemdavang.AnrealShop.modal.entity.product;

import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "product", "orderItem", "mediaList"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "product_reviews", indexes = {
        @Index(name = "idx_productreview_user_product", columnList = "user_id, product_id")
})
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", unique = true)
    private OrderItem orderItem; // Liên kết tới mục đơn hàng (để xác minh đã mua)

    @Column(nullable = false) // Ví dụ: 1, 2, 3, 4, 5
    private int rating; // Điểm đánh giá// Một OrderItem chỉ có một ProductReview

    @Column(columnDefinition = "TEXT")
    private String comment; // Nội dung bình luận

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ProductReviewMedia> mediaList = new ArrayList<>(); // Danh sách media đính kèm

    public void addMedia(ProductReviewMedia media) {
        if (mediaList == null) mediaList = new ArrayList<>();
        mediaList.add(media);
        media.setReview(this);
    }
}