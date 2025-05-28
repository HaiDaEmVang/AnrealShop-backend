package com.haiemdavang.AnrealShop.modal.entity;


import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.NotificationScope;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"receiver"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('SHOP', 'BUYER')")
    private NotificationScope scope;

    @Column(name = "item_count", columnDefinition = "INT DEFAULT 0")
    private int itemCount = 0;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl = "https://res.cloudinary.com/dlcjc36ow/image/upload/v1747916255/ImagError_jsv7hr.png";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "redirect_url", length = 255)
    private String redirectUrl;

    @Column(name = "is_read", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}