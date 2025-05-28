package com.haiemdavang.AnrealShop.modal.entity.category;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "display_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplayCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl = "https://res.cloudinary.com/dlcjc36ow/image/upload/v1747916255/ImagError_jsv7hr.png";
}