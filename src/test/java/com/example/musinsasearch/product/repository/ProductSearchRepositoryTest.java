package com.example.musinsasearch.product.repository;

import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProductSearchRepositoryTest {

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void 제품_검색_GROUP_BY() {
        //given
        //when
        long startTime = System.currentTimeMillis();
        productSearchRepository.searchProductLowestPricesByCategory();
        long endTime = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (endTime - startTime));

        //then
    }
}