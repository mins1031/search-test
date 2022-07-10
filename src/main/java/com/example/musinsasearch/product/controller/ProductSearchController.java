package com.example.musinsasearch.product.controller;

import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.common.exception.SearchResultEmptyException;
import com.example.musinsasearch.common.exception.WrongParameterException;
import com.example.musinsasearch.common.response.RestResponse;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestAndHighestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponse;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponses;
import com.example.musinsasearch.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
    public RestResponse<ProductLowestPriceAndBrandResponses> searchLowestPriceAndOneBrandInAllBrand() {
        ProductLowestPriceAndBrandResponses responses = productSearchService.searchLowestPriceInAllBrand();
        return RestResponse.of(HttpStatus.OK, responses);
    }

    @GetMapping(ProductSearchControllerPath.LOW_AND_HIGH_PRICE_BY_CATEGORY)
    public RestResponse<ProductLowestAndHighestPriceResponses> searchLowestAndHighestProductByCategory(@PathVariable String categoryName) {
        ProductLowestAndHighestPriceResponses response = productSearchService.searchLowestAndHighestProductByCategory(categoryName);
        return RestResponse.of(HttpStatus.OK, response);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundCategoryException.class)
    public RestResponse<Void> notExistCategoryException(NotFoundCategoryException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongParameterException.class)
    public RestResponse<Void> wrongParameterExceptionException(WrongParameterException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SearchResultEmptyException.class)
    public RestResponse<Void> searchResultEmptyException(SearchResultEmptyException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
