package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.category.CategoriesDTO;
import com.haiemdavang.AnrealShop.dto.category.CategoryRequest;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import com.haiemdavang.AnrealShop.service.Cloudinary.StorageService;
import com.haiemdavang.AnrealShop.service.ICategoryService;


import com.haiemdavang.AnrealShop.service.IDisplayCategory;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final StorageService storageService;
    private final IDisplayCategory  displayCategoryService;

    @PostMapping
    public ResponseEntity<CategoriesDTO> createCategory(
            @RequestBody CategoryRequest request){

        Category categoryResult = new Category();
        categoryResult.setName(request.getName());
        categoryResult.setDescription(request.getDescription());
        categoryResult.setUrlPath(request.getUrlPath());
        categoryResult.setUrlSlug(request.getUrlSlug());
        categoryResult.setLevel(request.getLevel());
        if (request.getIdParentCategory() != null) {
            Category parentCategory = categoryService.findCategoriesById(request.getIdParentCategory()).get();
            categoryResult.setParent(parentCategory);
        }
        categoryResult.setCreatedAt(LocalDateTime.now());
        categoryResult = this.categoryService.save(categoryResult);


//        String thumbnailUrl = storageService.store(image).join();
        DisplayCategory displayCategory = new DisplayCategory();
        displayCategory.setCategory(categoryResult);
        displayCategory.setThumbnailUrl(request.getImageUrl());
        displayCategoryService.save(displayCategory);

        CategoriesDTO categoriesDTO = this.categoryService.transferToDTO(categoryResult);

        return new ResponseEntity<>(categoriesDTO, HttpStatus.CREATED);
    }

    // api ny để laasy thông tin chi tiết category theo id ae nhé
    @GetMapping("/{id}")
    public ResponseEntity<CategoriesDTO> getCategoryById(@PathVariable String id){
        Optional<Category> optionalCategory = categoryService.findCategoriesById(id);
        if(!optionalCategory.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category category =  optionalCategory.get();

        CategoriesDTO categoriesDTO = this.categoryService.transferToDTO(category);
        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    // Api này để laasy danh mục con của danh mục cha ae nhé
    @GetMapping("/child/{id}")
    public ResponseEntity<List<CategoriesDTO>> getCategoryChildByParenntId(@PathVariable String id){
        List<Category> childCategory = this.categoryService.findByParentID(id);
        List<CategoriesDTO> childCategoryDTO = this.categoryService.transferListCategoriesToListCategoriesDTO(childCategory);
        return new ResponseEntity<>(childCategoryDTO, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<CategoriesDTO>> getAllCategory(){
        List<Category> listCategories = categoryService.findAllCategories();
        List<CategoriesDTO> listCategoriesDTO = categoryService.transferListCategoriesToListCategoriesDTO(listCategories);
        return new ResponseEntity<>(listCategoriesDTO, HttpStatus.OK);
    }






}
