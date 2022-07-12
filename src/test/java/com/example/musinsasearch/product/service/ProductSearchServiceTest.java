package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.helper.SearchDataHelper;
import com.example.musinsasearch.common.exception.SearchResultEmptyException;
import com.example.musinsasearch.common.exception.WrongParameterException;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestAndHighestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponse;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponses;
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
        //스니커즈는 최저가 9000이 A, G 두개
        int resultCount = 9;

        //when
        ProductCategorizeLowestPriceResponses responses = productSearchService.searchProductLowestPricesByCategory();

        //then
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses()).hasSize(resultCount);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(0).getCategoryName()).isEqualTo("상의");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(0).getBrandName()).isEqualTo("C");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(0).getPrice()).isEqualTo(10000);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(1).getCategoryName()).isEqualTo("아우터");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(1).getBrandName()).isEqualTo("E");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(1).getPrice()).isEqualTo(5000);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(2).getCategoryName()).isEqualTo("바지");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(2).getBrandName()).isEqualTo("D");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(2).getPrice()).isEqualTo(3000);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(3).getCategoryName()).isEqualTo("스니커즈");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(3).getBrandName()).isEqualTo("A");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(3).getPrice()).isEqualTo(9000);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(4).getCategoryName()).isEqualTo("스니커즈");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(4).getBrandName()).isEqualTo("G");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(4).getPrice()).isEqualTo(9000);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(5).getCategoryName()).isEqualTo("가방");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(5).getBrandName()).isEqualTo("A");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(5).getPrice()).isEqualTo(2000);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(6).getCategoryName()).isEqualTo("모자");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(6).getBrandName()).isEqualTo("D");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(6).getPrice()).isEqualTo(1500);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(7).getCategoryName()).isEqualTo("양말");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(7).getBrandName()).isEqualTo("I");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(7).getPrice()).isEqualTo(1700);
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(8).getCategoryName()).isEqualTo("액세서리");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(8).getBrandName()).isEqualTo("F");
        Assertions.assertThat(responses.getProductCategorizeLowestPriceResponses().get(8).getPrice()).isEqualTo(1900);
    }

    @DisplayName("한 브랜드에서 카테고리 상품중 최저가 상품의 합이 최소인 브랜드와 가격 조회")
    @Test
    public void 브랜드_카테고리별_상품최저가합_최저가_조회() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        int expectPrice = 36100;
        String brandName = "D";

        //when
        ProductLowestPriceAndBrandResponses responses = productSearchService.searchLowestPriceInAllBrand();

        //then
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses().get(0).getLowestAllProductSumPrice()).isEqualTo(expectPrice);
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses().get(0).getLowestBrandName()).isEqualTo(brandName);
    }

    @DisplayName("한 브랜드에서 카테고리 상품중 최저가 상품의 합이 최소인 브랜드와 가격 중복조회")
    @Test
    void 브랜드_카테고리별_상품최저가합_최저가_중복조회() {
        //given
        List<Brand> brands = brandRepository.saveAll(Arrays.asList(
                Brand.createBrand("A"),
                Brand.createBrand("B"),
                Brand.createBrand("C")
        ));

        List<Category> categories = categoryRepository.saveAll(Arrays.asList(
                Category.createCategory("상의"),
                Category.createCategory("하의"),
                Category.createCategory("신발")
        ));

        List<Product> products = productRepository.saveAll(Arrays.asList(
                Product.createProduct("product1", 1000, brands.get(0), categories.get(0)),
                Product.createProduct("product2", 2000, brands.get(0), categories.get(1)),
                Product.createProduct("product3", 3000, brands.get(0), categories.get(2)),
                Product.createProduct("product4", 4000, brands.get(1), categories.get(0)),
                Product.createProduct("product5", 5000, brands.get(1), categories.get(1)),
                Product.createProduct("product6", 6000, brands.get(1), categories.get(2)),
                Product.createProduct("product7", 1000, brands.get(2), categories.get(0)),
                Product.createProduct("product8", 2000, brands.get(2), categories.get(1)),
                Product.createProduct("product9", 3000, brands.get(2), categories.get(2))
        ));

        int expectPrice = 6000;
        String firstBrandName = "A";
        String secondBrandName = "C";

        //when
        ProductLowestPriceAndBrandResponses responses = productSearchService.searchLowestPriceInAllBrand();

        //then
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses()).hasSize(2);
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses().get(0).getLowestAllProductSumPrice()).isEqualTo(expectPrice);
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses().get(0).getLowestBrandName()).isEqualTo(firstBrandName);
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses().get(1).getLowestAllProductSumPrice()).isEqualTo(expectPrice);
        Assertions.assertThat(responses.getProductLowestPriceAndBrandResponses().get(1).getLowestBrandName()).isEqualTo(secondBrandName);
    }

    @DisplayName("카테고리에서 최대가와 브랜드, 최소가와 브랜드 조회")
    @Test
    public void 카테고리_최대가_최소가_브랜드_검색() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        int lowestPrice = 10000;
        String lowestBrand = "C";
        int highestPrice = 11400;
        String highestBrand = "I";

        List<Category> categories = categoryRepository.findAll();
        String categoryName = categories.get(0).getName();

        //when
        ProductLowestAndHighestPriceResponses productLowestAndHighestPriceResponses = productSearchService.searchLowestAndHighestProductByCategory(categoryName);

        //then
        Assertions.assertThat(productLowestAndHighestPriceResponses.getLowestResponses()).hasSize(1);
        Assertions.assertThat(productLowestAndHighestPriceResponses.getLowestResponses().get(0).getBrandName()).isEqualTo(lowestBrand);
        Assertions.assertThat(productLowestAndHighestPriceResponses.getLowestResponses().get(0).getProductPrice()).isEqualTo(lowestPrice);

        Assertions.assertThat(productLowestAndHighestPriceResponses.getHighestResponses()).hasSize(1);
        Assertions.assertThat(productLowestAndHighestPriceResponses.getHighestResponses().get(0).getBrandName()).isEqualTo(highestBrand);
        Assertions.assertThat(productLowestAndHighestPriceResponses.getHighestResponses().get(0).getProductPrice()).isEqualTo(highestPrice);
    }

    @DisplayName("[예외] 카테고리에서 최대가와 브랜드, 최소가와 브랜드 조회시 존재하지 않는 카테고리 이름이 요청된다.")
    @Test
    public void 예외_존재하지않는_카테고리_이름_요청() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        String notExistCategoryName = "없는 카테고리 이름";

        //when & then
        Assertions.assertThatThrownBy(() -> productSearchService.searchLowestAndHighestProductByCategory(notExistCategoryName)).isInstanceOf(NotFoundCategoryException.class);
    }

    @DisplayName("[예외] 카테고리에서 최대가와 브랜드, 최소가와 브랜드 조회시 파라미터가 부적절하다")
    @Test
    public void 예외_잘못된_카테고리_이름_요청() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        String wrongParameter = "";

        //when & then
        Assertions.assertThatThrownBy(() -> productSearchService.searchLowestAndHighestProductByCategory(wrongParameter)).isInstanceOf(WrongParameterException.class);
    }

    @DisplayName("[예외] 카테고리에서 최대가와 브랜드, 최소가와 브랜드 조회시 파라미터로 조회한 제품이 없는경우")
    @Test
    public void 예외카테고리_최대가_최소가_브랜드_검색() {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        Category newCategory = categoryRepository.save(Category.createCategory("newCategory"));

        //when & then
        Assertions.assertThatThrownBy(() -> productSearchService.searchLowestAndHighestProductByCategory(newCategory.getName())).isInstanceOf(SearchResultEmptyException.class);
    }

}