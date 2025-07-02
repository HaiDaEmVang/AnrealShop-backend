package com.haiemdavang.AnrealShop.dto.category;

import com.haiemdavang.AnrealShop.modal.entity.category.ShopCategoryItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

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
    private boolean hasChildren;
    private int productCount;
    private LocalDateTime createdAt;
    private String urlThumbnail;

}