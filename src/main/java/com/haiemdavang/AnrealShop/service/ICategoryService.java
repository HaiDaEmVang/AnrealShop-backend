package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.category.CategoriesDTO;
import com.haiemdavang.AnrealShop.dto.category.CategoryRequest;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Optional<Category> findCategoriesById(String id);
    Category save(Category category);
    CategoriesDTO transferToDTO(Category category);
    List<Category> findAllCategories();
    List<CategoriesDTO> transferListCategoriesToListCategoriesDTO(List<Category> listCategory);
    List<CategoriesDTO> getCategoryTree(String parentId);
    List<Category> findByParentID(String parentId);
    Category updateCategory(String id, CategoryRequest request);
    void deleteCategory(String id);
    void reorderCategory(String id, String direction);
}