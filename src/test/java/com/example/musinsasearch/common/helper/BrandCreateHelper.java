package com.example.musinsasearch.common.helper;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;

public class BrandCreateHelper {

    public static Brand 브랜드_생성 (BrandRepository brandRepository, String name) {
        return brandRepository.save(Brand.createBrand(name));
    }
}
