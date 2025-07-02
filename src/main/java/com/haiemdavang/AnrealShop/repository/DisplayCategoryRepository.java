package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisplayCategoryRepository extends JpaRepository<DisplayCategory, String> {

    Optional<DisplayCategory> findByCategory_Id(String categoryId);

}
