package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.category.BaseCategoryDto;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsCategory;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public BaseCategoryDto toBaseCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        return BaseCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .urlPath(category.getUrlPath())
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
                .build();
    }
}
