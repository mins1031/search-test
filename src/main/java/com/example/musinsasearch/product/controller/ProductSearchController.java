package com.example.musinsasearch.product.controller;

import com.example.musinsasearch.common.response.RestResponse;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestAndHighestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponse;
import com.example.musinsasearch.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductSearchService productSearchService;

    @GetMapping(ProductSearchControllerPath.LOW_PRICES_BY_CATEGORY)
    public RestResponse<ProductCategorizeLowestPriceResponses> searchProductLowestPricesByCategory() {
        ProductCategorizeLowestPriceResponses responses = productSearchService.searchProductLowestPricesByCategory();
        return RestResponse.of(HttpStatus.OK, responses);
    }

    @GetMapping(ProductSearchControllerPath.ALL_CATEGORY_LOW_PRICES_BY_BRAND)
    public RestResponse<ProductLowestPriceAndBrandResponse> searchLowestPriceAndOneBrandInAllBrand() {
        ProductLowestPriceAndBrandResponse response = productSearchService.searchLowestPriceInAllBrand();
        return RestResponse.of(HttpStatus.OK, response);
    }

    @GetMapping(ProductSearchControllerPath.LOW_AND_HIGH_PRICE_BY_CATEGORY)
    public RestResponse<ProductLowestAndHighestPriceResponses> searchLowestAndHighestProductByCategory(@PathVariable String categoryName) {
        ProductLowestAndHighestPriceResponses response = productSearchService.searchLowestAndHighestProductByCategory(categoryName);
        return RestResponse.of(HttpStatus.OK, response);
    }


}
