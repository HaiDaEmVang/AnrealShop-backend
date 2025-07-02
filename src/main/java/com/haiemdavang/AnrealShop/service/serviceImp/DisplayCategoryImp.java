package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import com.haiemdavang.AnrealShop.repository.DisplayCategoryRepository;
import com.haiemdavang.AnrealShop.service.IDisplayCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisplayCategoryImp implements IDisplayCategory {

    private final DisplayCategoryRepository displayCategoryRepository;

    @Override
    public DisplayCategory save(DisplayCategory displayCategory) {
        return displayCategoryRepository.save(displayCategory);
    }

    @Override
    public DisplayCategory findDisplayCategoryByIdCategory(String categoryId) {
        return displayCategoryRepository.findByCategory_Id(categoryId).get();
    }
}
