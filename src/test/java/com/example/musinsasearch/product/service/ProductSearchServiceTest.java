package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.BrandTestHelper;
import com.example.musinsasearch.common.CategoryTestHelper;
import com.example.musinsasearch.common.SearchDataHelper;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.ProductCategorizeLowestPriceResponse;
import com.example.musinsasearch.product.dto.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("h2")
class ProductSearchServiceTest {

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;


    @DisplayName("카테고리별 브랜드 상관없이 최저가 상품 조회")
    @Test
    public void 카테고리별_상품_최저가_조회() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);

        //when
        ProductCategorizeLowestPriceResponses responses = productSearchService.searchProductLowestPricesByCategory();

        //then
        List<Category> categories = categoryRepository.findAll();

        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses()).hasSize(categories.size());
    }
}