package com.example.musinsasearch.integrate;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.helper.BrandCreateHelper;
import com.example.musinsasearch.common.helper.CategoryCreateHelper;
import com.example.musinsasearch.common.helper.ProductCreateHelper;
import com.example.musinsasearch.product.controller.ProductCommandControllerPath;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.request.ProductCreateRequest;
import com.example.musinsasearch.product.dto.request.ProductDeleteRequest;
import com.example.musinsasearch.product.dto.request.ProductUpdateRequest;
import com.example.musinsasearch.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public class ProductCommandIntegrateTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("상품 생성 통합테스트")
    @Test
    public void 상품_생성_통합테스트() throws Exception {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;

        ProductCreateRequest productCreateRequest = new ProductCreateRequest(productName, productPrice, brand.getNum(), category.getNum());

        //when
        mockMvc.perform(post(ProductCommandControllerPath.PRODUCT_SAVE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productCreateRequest)))
                .andExpectAll(status().isOk())
                .andDo(print());

        //then
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(1);
    }

    @DisplayName("상품 수정 통합테스트")
    @Test
    public void 상품_수정_통합테스트() throws Exception {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        Product product = ProductCreateHelper.상품_생성(productRepository, "productName", 20000, brand, category);
        String updateName = "udpateName";
        int updatePrice = 30000;

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(product.getNum(), updateName, updatePrice);

        //when
        mockMvc.perform(patch(ProductCommandControllerPath.PRODUCT_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productUpdateRequest)))
                .andExpectAll(status().isOk())
                .andDo(print());

        //then
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products.get(0).getName()).isEqualTo(updateName);
        Assertions.assertThat(products.get(0).getPrice()).isEqualTo(updatePrice);
    }

    @DisplayName("상품 삭제 통합테스트")
    @Test
    public void 상품_삭제_통합테스트() throws Exception {
        //given
        Brand brand = BrandCreateHelper.브랜드_생성(brandRepository, "브랜드");
        Category category = CategoryCreateHelper.카테고리_생성(categoryRepository, "카테고리");
        String productName = "productName";
        int productPrice = 20000;
        Product product = ProductCreateHelper.상품_생성(productRepository, productName, productPrice, brand, category);

        ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest(product.getNum());

        //when
        mockMvc.perform(delete(ProductCommandControllerPath.PRODUCT_DELETE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productDeleteRequest)))
                .andExpectAll(status().isOk())
                .andDo(print());

        //then
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).isEmpty();
    }
}
