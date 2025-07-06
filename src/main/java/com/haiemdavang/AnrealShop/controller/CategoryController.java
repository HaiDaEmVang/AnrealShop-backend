package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.category.CategoriesDTO;
import com.haiemdavang.AnrealShop.dto.category.CategoryRequest;
import com.haiemdavang.AnrealShop.dto.category.ReorderRequest;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import com.haiemdavang.AnrealShop.service.Cloudinary.StorageService;
import com.haiemdavang.AnrealShop.service.ICategoryService;
import com.haiemdavang.AnrealShop.service.IDisplayCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final StorageService storageService;
    private final IDisplayCategory displayCategoryService;

    @PostMapping
    public ResponseEntity<CategoriesDTO> createCategory(@RequestBody CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .urlPath(request.getUrlPath())
                .urlSlug(request.getUrlSlug())
                .level(request.getLevel())
                .active(request.isActive())
                .order(request.getOrder())
                .createdAt(LocalDateTime.now())
                .build();

        if (request.getIdParentCategory() != null) {
            Category parentCategory = categoryService.findCategoriesById(request.getIdParentCategory())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parentCategory);
        }

        Category savedCategory = categoryService.save(category);

        DisplayCategory displayCategory = DisplayCategory.builder()
                .category(savedCategory)
                .thumbnailUrl(request.getImageUrl())
                .publicId(request.getPublicId())
                .build();
        displayCategoryService.save(displayCategory);

        CategoriesDTO categoriesDTO = categoryService.transferToDTO(savedCategory);
        return new ResponseEntity<>(categoriesDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriesDTO> getCategoryById(@PathVariable String id) {
        Category category = categoryService.findCategoriesById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CategoriesDTO categoriesDTO = categoryService.transferToDTO(category);
        categoriesDTO.setChildren(categoryService.getCategoryTree(id));
        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    @GetMapping("/child/{id}")
    public ResponseEntity<List<CategoriesDTO>> getCategoryChildByParentId(@PathVariable String id) {
        List<CategoriesDTO> childCategoryDTO = categoryService.getCategoryTree(id);
        return new ResponseEntity<>(childCategoryDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoriesDTO>> getAllCategories(@RequestParam(value = "flat", defaultValue = "false") boolean flat) {
        if (flat) {
            List<Category> listCategories = categoryService.findAllCategories();
            List<CategoriesDTO> listCategoriesDTO = categoryService.transferListCategoriesToListCategoriesDTO(listCategories);
            return new ResponseEntity<>(listCategoriesDTO, HttpStatus.OK);
        } else {
            List<CategoriesDTO> categoryTree = categoryService.getCategoryTree(null);
            return new ResponseEntity<>(categoryTree, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriesDTO> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        Category updatedCategory = categoryService.updateCategory(id, request);
        CategoriesDTO categoriesDTO = categoryService.transferToDTO(updatedCategory);
        categoriesDTO.setChildren(categoryService.getCategoryTree(id));
        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/reorder")
    public ResponseEntity<CategoriesDTO> reorderCategory(@PathVariable String id, @RequestBody ReorderRequest request) {
        categoryService.reorderCategory(id, request.getDirection());
        Category category = categoryService.findCategoriesById(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy cảtegory"));
        CategoriesDTO categoriesDTO = categoryService.transferToDTO(category);
        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    @DeleteMapping("/upload/{publicId}")
    public ResponseEntity<Void> deleteImage(@PathVariable String publicId) {
        storageService.deleteImage(publicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}