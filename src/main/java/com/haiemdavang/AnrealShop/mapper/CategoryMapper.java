package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.category.BaseCategoryDto;
import com.haiemdavang.AnrealShop.dto.category.CategoryModalSelectedDto;
import com.haiemdavang.AnrealShop.tech.elasticsearch.document.EsCategory;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public CategoryModalSelectedDto toCategoryModalSelectedDto(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryModalSelectedDto.builder()
                .id(category.getId())
                .name(category.getName())
                .urlPath(category.getUrlPath())
                .urlSlug(category.getUrlSlug())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .hasChildren(category.isHasChildren())
                .level(category.getLevel())
                .build();
    }

    public BaseCategoryDto toBaseCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        return BaseCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .urlPath(category.getUrlPath())
                .urlSlug(category.getUrlSlug())
                .build();
    }
    public BaseCategoryDto toBaseCategoryDto(EsCategory category) {
        if (category == null) {
            return null;
        }

        return BaseCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .urlPath(category.getUrlPath())
                .urlSlug(category.getUrlSlug())
                .build();
    }
    public EsCategory toEsCategory(BaseCategoryDto category) {
        if (category == null) {
            return null;
        }

        return EsCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .urlPath(category.getUrlPath())
                .urlSlug(category.getUrlSlug())
                .build();
    }

}
