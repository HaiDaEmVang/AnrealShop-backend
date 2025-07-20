package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.category.BaseCategoryDto;
import com.haiemdavang.AnrealShop.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/suggest")
    public ResponseEntity<List<BaseCategoryDto>> getCategorySuggest(@RequestParam(required = false) String keyword) {
        List<BaseCategoryDto> categories = categoryService.getCategorySuggest(keyword);
        return ResponseEntity.ok(categories);
    }

}
