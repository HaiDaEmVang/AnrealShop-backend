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
    private User user; // Người dùng viết đánh giá

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Sản phẩm được đánh giá

    /**
     * Liên kết với OrderItem để xác minh đánh giá này từ một giao dịch mua hàng cụ thể.
     * Cột order_item_id trong DB có thể có ràng buộc UNIQUE.
     * Nếu OrderItem bị xóa, orderItem trong ProductReview có thể được set là null
     * (nếu FK trong DB là ON DELETE SET NULL và order_item_id cho phép NULL).
     * Hoặc có thể là ON DELETE CASCADE/RESTRICT tùy logic.
     * Hiện tại DDL có order_item_id UNIQUE (NOT NULL).
     */
    @OneToOne(fetch = FetchType.LAZY) // Một OrderItem chỉ có một ProductReview
    @JoinColumn(name = "order_item_id", unique = true) // unique = true phản ánh ràng buộc UNIQUE trong DB
    private OrderItem orderItem; // Liên kết tới mục đơn hàng (để xác minh đã mua)

    @Column(nullable = false) // Ví dụ: 1, 2, 3, 4, 5
    private int rating; // Điểm đánh giá

    @Column(columnDefinition = "TEXT")
    private String comment; // Nội dung bình luận

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductReviewMedia> mediaList = new ArrayList<>(); // Danh sách media đính kèm

    // Helper method
    public void addMedia(ProductReviewMedia media) {
        if (mediaList == null) mediaList = new ArrayList<>();
        mediaList.add(media);
        media.setReview(this);
    }
}