package com.example.musinsasearch.integrate;

import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.basetest.IntegrateBaseTest;
import com.example.musinsasearch.common.helper.SearchDataHelper;
import com.example.musinsasearch.product.controller.ProductSearchControllerPath;
import com.example.musinsasearch.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public class ProductSearchIntegrateTest extends IntegrateBaseTest {

    @DisplayName("카테고리별 브랜드 상관없이 최저가 상품 조회 통합테스트")
    @Test
    public void 카테고리별_최저가_상품_조회_통합테스트() throws Exception {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        //스니커즈는 최저가 9000이 A, G 두개
        int resultCount = 9;
        int totalPrice = 34100;

        //when & then
        mockMvc.perform(get(ProductSearchControllerPath.LOW_PRICES_BY_CATEGORY)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.productCategorizeLowestPriceResponses", hasSize(resultCount)))
                .andExpect(jsonPath("result.totalLowestPriceByCategory").value(totalPrice))
                .andDo(print());
    }

    @DisplayName("한 브랜드에서 카테고리 상품중 최저가 상품의 합이 최소인 브랜드와 가격 조회")
    @Test
    public void 브랜드별_카테고리_상품최저가합_최저가_조회_통합테스트() throws Exception {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        String resultBrandName = "D";
        int resultLowestPrice = 36100;

        //when & then
        mockMvc.perform(get(ProductSearchControllerPath.ALL_CATEGORY_LOW_PRICES_BY_BRAND)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.lowestBrandName").value(resultBrandName))
                .andExpect(jsonPath("result.lowestAllProductSumPrice").value(resultLowestPrice))
                .andDo(print());
    }

    @DisplayName("카테고리에서 최대가와 브랜드, 최소가와 브랜드 조회")
    @Test
    public void 카테고리_최대가_최소가_브랜드_조회_통합테스트() throws Exception {
        //given
        SearchDataHelper.검색_데이터_저장(categoryRepository, brandRepository, productRepository);
        String searcgCategoryName = "상의";

        String resultLowestBrandName = "C";
        int resultLowestPrice = 10000;
        String resultHighestBrandName = "I";
        int resultHighestPrice = 11400;

        //when & then
        String path = ProductSearchControllerPath.LOW_AND_HIGH_PRICE_BY_CATEGORY.replace("{categoryName}", searcgCategoryName);
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.lowestResponses", hasSize(1)))
                .andExpect(jsonPath("result.lowestResponses.[0].brandName").value(resultLowestBrandName))
                .andExpect(jsonPath("result.lowestResponses.[0].productPrice").value(resultLowestPrice))
                .andExpect(jsonPath("result.highestResponses", hasSize(1)))
                .andExpect(jsonPath("result.highestResponses.[0].brandName").value(resultHighestBrandName))
                .andExpect(jsonPath("result.highestResponses.[0].productPrice").value(resultHighestPrice))
                .andDo(print());
    }
}