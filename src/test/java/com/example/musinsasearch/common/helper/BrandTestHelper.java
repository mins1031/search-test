package com.example.musinsasearch.common.helper;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;

public class BrandTestHelper {

    public static void 브랜드_등록(BrandRepository brandRepository) {
        Brand a = brandRepository.save(Brand.createBrand("A"));
        Brand b = brandRepository.save(Brand.createBrand("B"));
        Brand c = brandRepository.save(Brand.createBrand("C"));
        Brand d = brandRepository.save(Brand.createBrand("D"));
        Brand e = brandRepository.save(Brand.createBrand("E"));
        Brand f = brandRepository.save(Brand.createBrand("F"));
        Brand g = brandRepository.save(Brand.createBrand("G"));
        Brand h = brandRepository.save(Brand.createBrand("H"));
        Brand i = brandRepository.save(Brand.createBrand("I"));
    }
}
