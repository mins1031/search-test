package com.example.musinsasearch.product.repository;

import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.helper.SearchDataHelper;
import com.example.musinsasearch.product.dto.raw.ProductBrandNumAndNameRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestPriceByCategoryRawDto;
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
class ProductSearchRepositoryTest {

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("각 카테고리의 최저가를 조회한다.")
    @Test
    public void 카테고리별_상품_최저가조회() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        List<ProductLowestPriceByCategoryRawDto> productLowestPriceByCategoryRawDtos = Arrays.asList(
                new ProductLowestPriceByCategoryRawDto(1L, "상의", 10000),
                new ProductLowestPriceByCategoryRawDto(2L, "아우터", 5000),
                new ProductLowestPriceByCategoryRawDto(3L, "바지", 3000),
                new ProductLowestPriceByCategoryRawDto(4L, "스니커즈", 9000),
                new ProductLowestPriceByCategoryRawDto(5L, "가방", 2000),
                new ProductLowestPriceByCategoryRawDto(6L, "모자", 1500),
                new ProductLowestPriceByCategoryRawDto(7L, "양말", 1700),
                new ProductLowestPriceByCategoryRawDto(8L, "액세서리", 1900)
        );

        //when
        List<ProductLowestPriceByCategoryRawDto> resultRawDtos = productSearchRepository.findLowestPriceByAllCategory();

        //then
        Assertions.assertThat(resultRawDtos).hasSize(productLowestPriceByCategoryRawDtos.size());
        for (int idx = 0; idx < resultRawDtos.size(); idx++) {
            Assertions.assertThat(resultRawDtos.get(idx).getCategoryNum()).isEqualTo(productLowestPriceByCategoryRawDtos.get(idx).getCategoryNum());
            Assertions.assertThat(resultRawDtos.get(idx).getCategoryName()).isEqualTo(productLowestPriceByCategoryRawDtos.get(idx).getCategoryName());
            Assertions.assertThat(resultRawDtos.get(idx).getLowestPrice()).isEqualTo(productLowestPriceByCategoryRawDtos.get(idx).getLowestPrice());
        }
    }

    @DisplayName("각 카테고리의 상품들을 카테고리와 카테고리별 최저가를 통해 조회한다.")
    @Test
    public void 카테고리_최저가로_상품조회() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        long categoryNum = 4L;
        int lowestPrice = 9000;

        //when
        List<ProductBrandNumAndNameRawDto> lowestPriceAndBrandByCategories = productSearchRepository.findLowestPriceAndBrandByCategory(lowestPrice, categoryNum);

        //then
        Assertions.assertThat(lowestPriceAndBrandByCategories).hasSize(2);
    }
}