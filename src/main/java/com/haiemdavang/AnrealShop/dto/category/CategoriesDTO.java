package com.haiemdavang.AnrealShop.dto.category;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CategoriesDTO {
    private String id;
    private String name;
    private String parentId;
    private String description;
    private String urlSlug;
    private String urlPath;
    private Integer level;
    private boolean active;
    private int order;
    private boolean hasChildren;
    private int productCount;
    private LocalDateTime createdAt;
    private String urlThumbnail;
    private String publicId;
    private List<CategoriesDTO> children;
}