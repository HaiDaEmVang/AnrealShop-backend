package com.haiemdavang.AnrealShop.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private String name;
    private String urlSlug;
    private String urlPath;
    private String idParentCategory;
    private String description;
    private int level;
    private String imageUrl;


}
