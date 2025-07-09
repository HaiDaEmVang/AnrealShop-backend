package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.category.BaseCategoryDto;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;

import java.util.List;

public interface ICategoryService {
    Category findById(String categoryId);

    List<Category> getAllCategories();

    List<BaseCategoryDto> getCategorySuggest(String keyword);
}
