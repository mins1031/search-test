package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.exception.NotFoundBrandException;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.exception.SearchResultEmptyException;
import com.example.musinsasearch.common.helper.BrandCreateHelper;
import com.example.musinsasearch.common.helper.CategoryCreateHelper;
import com.example.musinsasearch.common.helper.ProductCreateHelper;
import com.example.musinsasearch.common.helper.SearchDataHelper;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.request.ProductCreateRequest;
import com.example.musinsasearch.product.dto.request.ProductDeleteRequest;
import com.example.musinsasearch.product.dto.request.ProductUpdateRequest;
import com.example.musinsasearch.product.exception.NotFoundProductException;
import com.example.musinsasearch.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("h2")
class ProductCommandServiceTest {

    @Autowired
    private ProductCommandService productCommandService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 생성한다")
    @Test
    public void 상품_생성() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;

        ProductCreateRequest productCreateRequest = new ProductCreateRequest(productName, productPrice, brand.getNum(), category.getNum());

        //when
        productCommandService.createProduct(productCreateRequest);

        //then
        Product product = productRepository.findAll().get(0);
        Assertions.assertThat(product.getName()).isEqualTo(productCreateRequest.getProductName());
        Assertions.assertThat(product.getPrice()).isEqualTo(productCreateRequest.getProductPrice());
    }

    @DisplayName("[예외] 상품생성시 없는 카테고리를 요청한 경우")
    @Test
    public void 예외_상품생성시_없는_카테고리() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;

        ProductCreateRequest productCreateRequest = new ProductCreateRequest(productName, productPrice, brand.getNum(), category.getNum() + 1);

        //when & then
        Assertions.assertThatThrownBy(() -> productCommandService.createProduct(productCreateRequest)).isInstanceOf(NotFoundCategoryException.class);
    }

    @DisplayName("[예외] 상품생성시 없는 브랜드를 요청한 경우")
    @Test
    public void 예외_상품생성시_없는_브랜드() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;

        ProductCreateRequest productCreateRequest = new ProductCreateRequest(productName, productPrice, brand.getNum() + 1, category.getNum());

        //when & then
        Assertions.assertThatThrownBy(() -> productCommandService.createProduct(productCreateRequest)).isInstanceOf(NotFoundBrandException.class);
    }

    @DisplayName("상품을 수정한다")
    @Test
    public void 상품_수정() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        Product product = ProductCreateHelper.상품_생성(productRepository, "productName", 20000, brand, category);
        String updateName = "udpateName";
        int updatePrice = 30000;

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(product.getNum(), updateName, updatePrice);

        //when
        productCommandService.updateProduct(productUpdateRequest);

        //then
        Product updatedProduct = productRepository.findById(product.getNum()).get();
        Assertions.assertThat(updatedProduct.getName()).isEqualTo(productUpdateRequest.getProductName());
        Assertions.assertThat(updatedProduct.getPrice()).isEqualTo(productUpdateRequest.getProductPrice());
    }

    @DisplayName("[예외] 상품 수정시 없는 상품정보를 요청한 경우")
    @Test
    public void 예외_상품수정시_없는_상품() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        Product product = ProductCreateHelper.상품_생성(productRepository, "productName", 20000, brand, category);
        String updateName = "udpateName";
        int updatePrice = 30000;

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(product.getNum() + 1, updateName, updatePrice);

        //when & then
        Assertions.assertThatThrownBy(() -> productCommandService.updateProduct(productUpdateRequest)).isInstanceOf(NotFoundProductException.class);
    }

    @DisplayName("상품을 삭제한다")
    @Test
    public void 상품_삭제() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;
        Product product = ProductCreateHelper.상품_생성(productRepository, productName, productPrice, brand, category);

        ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest(product.getNum());

        //when
        productCommandService.deleteProduct(productDeleteRequest);

        //then
        Optional<Product> deletedProduct = productRepository.findById(product.getNum());
        Assertions.assertThat(deletedProduct.isPresent()).isFalse();
    }

    @DisplayName("[예외] 상품 삭제시 없는 상품정보를 요청한 경우")
    @Test
    public void 예외_상품삭제_없는_상품() {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;
        Product product = ProductCreateHelper.상품_생성(productRepository, productName, productPrice, brand, category);

        ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest(product.getNum() + 1);

        //when & then
        Assertions.assertThatThrownBy(() -> productCommandService.deleteProduct(productDeleteRequest)).isInstanceOf(NotFoundProductException.class);
    }
}
