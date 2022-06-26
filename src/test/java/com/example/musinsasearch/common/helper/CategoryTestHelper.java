package com.example.musinsasearch.common.helper;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

public class CategoryTestHelper {

    public static void 카테고리_등록(CategoryRepository categoryRepository) {
        Category 상의 = categoryRepository.save(Category.createCategory("상의"));
        Category 아우터 = categoryRepository.save(Category.createCategory("아우터"));
        Category 바지 = categoryRepository.save(Category.createCategory("바지"));
        Category 스니커즈 = categoryRepository.save(Category.createCategory("스니커즈"));
        Category 가방 = categoryRepository.save(Category.createCategory("가방"));
        Category 모자 = categoryRepository.save(Category.createCategory("모자"));
        Category 양말 = categoryRepository.save(Category.createCategory("양말"));
        Category 액세서리량= categoryRepository.save(Category.createCategory("액세서리"));
    }
}
