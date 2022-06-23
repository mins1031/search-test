package com.example.musinsasearch.product.controller;

import com.example.musinsasearch.common.response.RestResponse;
import com.example.musinsasearch.product.dto.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.service.ProductSearchService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductSearchService productSearchService;

    @GetMapping(ProductSearchControllerPath.MIN_PRICES_BY_CATEGORY_PATH)
    public RestResponse<ProductCategorizeLowestPriceResponses> searchProductLowestPricesByCategory() {
        ProductCategorizeLowestPriceResponses responses = productSearchService.searchProductLowestPricesByCategory();
        return RestResponse.of(HttpStatus.OK, responses);
    }


}
