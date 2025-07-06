package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.category.CategoriesDTO;
import com.haiemdavang.AnrealShop.dto.category.CategoryRequest;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import com.haiemdavang.AnrealShop.repository.CategoryRepository;
import com.haiemdavang.AnrealShop.repository.DisplayCategoryRepository;
import com.haiemdavang.AnrealShop.service.Cloudinary.StorageService;
import com.haiemdavang.AnrealShop.service.ICategoryService;
import com.haiemdavang.AnrealShop.service.IDisplayCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final DisplayCategoryRepository displayCategoryRepository;
    private final StorageService storageService;
    private final IDisplayCategory displayCategoryService;

    @Override
    public Optional<Category> findCategoriesById(String id) {
        return categoryRepository.findCategoriesById(id);
    }

    @Override
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
        categoriesDTO.setActive(category.isActive());
        categoriesDTO.setOrder(category.getOrder());
        categoriesDTO.setParentId(category.getParent() != null ? category.getParent().getId() : null);

        Optional<DisplayCategory> displayCategory = displayCategoryRepository.findByCategory_Id(category.getId());
        if (displayCategory.isPresent()) {
            categoriesDTO.setUrlThumbnail(displayCategory.get().getThumbnailUrl());
            categoriesDTO.setPublicId(displayCategory.get().getPublicId());
        }

        return categoriesDTO;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoriesDTO> transferListCategoriesToListCategoriesDTO(List<Category> listCategory) {
        List<CategoriesDTO> listCategoriesDTO = new ArrayList<>();
        for (Category category : listCategory) {
            listCategoriesDTO.add(transferToDTO(category));
        }
        return listCategoriesDTO;
    }

    @Override
    public List<Category> findByParentID(String parentId) {
        return categoryRepository.findByParent_Id(parentId);
    }

    @Override
    public List<CategoriesDTO> getCategoryTree(String parentId) {
        List<Category> categories = parentId == null ? categoryRepository.findByParent_Id(null) : categoryRepository.findByParent_Id(parentId);
        List<CategoriesDTO> result = new ArrayList<>();

        for (Category category : categories) {
            CategoriesDTO dto = transferToDTO(category);
            List<CategoriesDTO> children = getCategoryTree(category.getId());
            dto.setChildren(children);
            if (!children.isEmpty()) {
                category.setHasChildren(true);
                categoryRepository.save(category);
            }
            result.add(dto);
        }

        return result;
    }

    @Override
    public Category updateCategory(String id, CategoryRequest request) {
        Optional<Category> optionalCategory = categoryRepository.findCategoriesById(id);
        if (!optionalCategory.isPresent()) {
            throw new RuntimeException("Category not found");
        }

        Category category = optionalCategory.get();

        // Kiểm tra slug
        if (request.getUrlSlug() != null && !request.getUrlSlug().equals(category.getUrlSlug())) {
            if (categoryRepository.findByUrlSlug(request.getUrlSlug()).isPresent()) {
                throw new RuntimeException("Slug already exists");
            }
        }

        // Kiểm tra parentId
        if (request.getIdParentCategory() != null && !request.getIdParentCategory().equals(category.getParent() != null ? category.getParent().getId() : null)) {
            Optional<Category> parentOpt = categoryRepository.findCategoriesById(request.getIdParentCategory());
            if (!parentOpt.isPresent()) {
                throw new RuntimeException("Parent category not found");
            }
            // Kiểm tra không cho phép parent là con của chính nó
            List<String> descendantIds = getDescendantIds(category.getId());
            if (descendantIds.contains(request.getIdParentCategory())) {
                throw new RuntimeException("Cannot set a descendant as parent");
            }
            category.setParent(parentOpt.get());
            category.setLevel(parentOpt.get().getLevel() + 1);
        } else if (request.getIdParentCategory() == null && category.getParent() != null) {
            category.setParent(null);
            category.setLevel(0);
        }

        category.setName(request.getName() != null ? request.getName() : category.getName());
        category.setDescription(request.getDescription() != null ? request.getDescription() : category.getDescription());
        category.setUrlSlug(request.getUrlSlug() != null ? request.getUrlSlug() : category.getUrlSlug());
        category.setUrlPath(request.getUrlPath() != null ? request.getUrlPath() : category.getUrlPath());
        category.setActive(request.isActive());
        category.setOrder(request.getOrder() != 0 ? request.getOrder() : category.getOrder());
        category.setLevel(request.getLevel() != 0 ? request.getLevel() : category.getLevel());

        // Cập nhật DisplayCategory
        Optional<DisplayCategory> displayCategoryOpt = displayCategoryRepository.findByCategory_Id(category.getId());
        DisplayCategory displayCategory = null;
        if (displayCategoryOpt.isPresent()) {
            displayCategory = displayCategoryOpt.get();
            if (request.getImageUrl() != null && !request.getImageUrl().equals(displayCategory.getThumbnailUrl())) {
                // Xóa hình ảnh cũ trên Cloudinary nếu có
                if (displayCategory.getPublicId() != null) {
                    storageService.deleteImage(displayCategory.getPublicId());
                }
                displayCategory.setThumbnailUrl(request.getImageUrl());
                displayCategory.setPublicId(request.getPublicId());
            }
        } else if (request.getImageUrl() != null) {
            displayCategory = DisplayCategory.builder()
                    .category(category)
                    .thumbnailUrl(request.getImageUrl())
                    .publicId(request.getPublicId())
                    .build();
        }

        Category savedCategory = categoryRepository.save(category);
        if (displayCategory != null) {
            displayCategoryService.save(displayCategory);
        }

        return savedCategory;
    }

    @Override
    public void deleteCategory(String id) {
        Optional<Category> optionalCategory = categoryRepository.findCategoriesById(id);
        if (!optionalCategory.isPresent()) {
            throw new RuntimeException("Category not found");
        }

        Category category = optionalCategory.get();
        List<String> descendantIds = getDescendantIds(id);

        // Xóa hình ảnh trên Cloudinary cho danh mục và danh mục con
        Optional<DisplayCategory> displayCategory = displayCategoryRepository.findByCategory_Id(id);
        if (displayCategory.isPresent() && displayCategory.get().getPublicId() != null) {
            storageService.deleteImage(displayCategory.get().getPublicId());
        }
        for (String descendantId : descendantIds) {
            Optional<DisplayCategory> descDisplayCategory = displayCategoryRepository.findByCategory_Id(descendantId);
            if (descDisplayCategory.isPresent() && descDisplayCategory.get().getPublicId() != null) {
                storageService.deleteImage(descDisplayCategory.get().getPublicId());
            }
            displayCategoryRepository.deleteById(descendantId);
        }

        categoryRepository.deleteById(id);
        categoryRepository.deleteAllByIdInBatch(descendantIds);
    }

    @Override
    public void reorderCategory(String id, String direction) {
        Optional<Category> optionalCategory = categoryRepository.findCategoriesById(id);
        if (!optionalCategory.isPresent()) {
            throw new RuntimeException("Category not found");
        }

        Category category = optionalCategory.get();
        List<Category> siblings = categoryRepository.findByParent_Id(category.getParent() != null ? category.getParent().getId() : null);
        siblings.sort((a, b) -> Integer.compare(a.getOrder(), b.getOrder()));

        int index = siblings.indexOf(category);
        if ((direction.equals("up") && index == 0) || (direction.equals("down") && index == siblings.size() - 1)) {
            throw new RuntimeException("Cannot move category further");
        }

        int swapIndex = direction.equals("up") ? index - 1 : index + 1;
        Category swapCategory = siblings.get(swapIndex);

        int tempOrder = category.getOrder();
        category.setOrder(swapCategory.getOrder());
        swapCategory.setOrder(tempOrder);

        categoryRepository.save(category);
        categoryRepository.save(swapCategory);
    }

    private List<String> getDescendantIds(String categoryId) {
        List<String> result = new ArrayList<>();
        List<Category> children = categoryRepository.findByParent_Id(categoryId);
        for (Category child : children) {
            result.add(child.getId());
            result.addAll(getDescendantIds(child.getId()));
        }
        return result;
    }
}