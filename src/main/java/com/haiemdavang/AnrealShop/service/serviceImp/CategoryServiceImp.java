package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.category.BaseCategoryDto;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsCategory;
import com.haiemdavang.AnrealShop.elasticsearch.service.CategoryIndexerService;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.mapper.CategoryMapper;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.repository.CategoryRepository;
import com.haiemdavang.AnrealShop.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryIndexerService esCategoryIndexerService;
    private final CategoryMapper categoryMapper;

    @Override
    public Category findByIdAndThrow(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("CATEGORY_NOT_FOUND"));
    }

    @Override
    public Category findByIdOrUrlSlug(String categoryId) {
        return categoryRepository.findByIdOrUrlSlug(categoryId, categoryId)
                .orElseThrow(() -> new BadRequestException("CATEGORY_NOT_FOUND"));
    }

    @Override
    public Category findById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<BaseCategoryDto> getCategorySuggest(String keyword) {
        List<EsCategory> categories = esCategoryIndexerService.getCategoriesByKeyword(keyword);
        return categories.stream().map(categoryMapper::toBaseCategoryDto)
                .toList();
    }

    @Override
    public Set<BaseCategoryDto> getCategorySuggestByProductName(String keyword) {
        Set<EsCategory> categories = esCategoryIndexerService.getCategoriesByProductName(keyword, null);
        return categories.stream().map(categoryMapper::toBaseCategoryDto)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existsById(String categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public Category getReferenceById(String categoryId) {
        return categoryRepository.getReferenceById(categoryId);
    }


}
