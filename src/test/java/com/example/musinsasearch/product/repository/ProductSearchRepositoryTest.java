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
        List<Integer> byProductPricesByCategory = productSearchRepository.findByProductPricesByCategory();
        long endTime = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (endTime - startTime));

        //then
        for (Integer integer : byProductPricesByCategory) {
            System.out.println(integer);
        }
    }

    @Test
    public void 제품_검색_() {
        //given
        //when
        long startTime = System.currentTimeMillis();
        List<Category> categories = categoryRepository.findAll();
        List<Integer> prices = new ArrayList<>();

        for (Category category : categories) {
            List<Integer> tempPrices = productSearchRepository.findByProductPriceByCategory(category.getNum());
            System.out.println(prices.size());
            prices.add(tempPrices.get(0));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (endTime - startTime));

        //then
        for (Integer integer : prices) {
            System.out.println(integer);
        }
    }
}