package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.category.CategoriesDTO;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import com.haiemdavang.AnrealShop.repository.CategoryRepository;
import com.haiemdavang.AnrealShop.repository.DisplayCategoryRepository;
import com.haiemdavang.AnrealShop.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final DisplayCategoryRepository  displayCategoryRepository;


    @Override
    public Optional<Category> findCategoriesById(String id) {
        return categoryRepository.findCategoriesById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }



    @Override
    public CategoriesDTO transferToDTO(Category category) {

        CategoriesDTO categoriesDTO = new CategoriesDTO();


        categoriesDTO.setId(category.getId());
        categoriesDTO.setName(category.getName());
        categoriesDTO.setDescription(category.getDescription());
        categoriesDTO.setHasChildren(category.isHasChildren());
        categoriesDTO.setCreatedAt(category.getCreatedAt());
        categoriesDTO.setProductCount(category.getProductCount());
        categoriesDTO.setUrlPath(category.getUrlPath());
        categoriesDTO.setUrlSlug(category.getUrlSlug());
        categoriesDTO.setLevel(category.getLevel());

        // chỗ này là lấy ảnh mapping vào CategoriesDTO nhé ae
        Optional<DisplayCategory> displayCategory = displayCategoryRepository.findByCategory_Id(category.getId());
        if (displayCategory.isPresent()) {
            categoriesDTO.setUrlThumbnail(displayCategory.get().getThumbnailUrl());
        }

        if (category.getParent() != null) {
            Category parentCategory = category.getParent();
            parentCategory.setHasChildren(true);
            categoryRepository.save(parentCategory);
            categoriesDTO.setParentId(category.getParent().getId());
        }


        return categoriesDTO;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    // này để mapping list category sang list kieeru categoryDTO nhé ae :V
    @Override
    public List<CategoriesDTO> transferListCategoriesToListCategoriesDTO(List<Category> listCategory) {
        List<CategoriesDTO> listCategoriesDTO = new ArrayList<>();
        for (Category category : listCategory) {
            CategoriesDTO categoriesDTO = new CategoriesDTO();
            categoriesDTO = transferToDTO(category);
            listCategoriesDTO.add(categoriesDTO);
        }
        return listCategoriesDTO;
    }

    @Override
    public List<Category> findByParentID(String parentId) {
        return this.categoryRepository.findByParent_Id(parentId);
    }
}
