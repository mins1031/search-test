package com.example.musinsasearch.common.helper;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;

public class CategoryCreateHelper {

    public static Category 카테고리_생성 (CategoryRepository categoryRepository, String name) {
        return categoryRepository.save(Category.createCategory(name));
    }
}
