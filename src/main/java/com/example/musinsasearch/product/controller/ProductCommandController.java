package com.example.musinsasearch.product.controller;

import com.example.musinsasearch.brand.exception.NotFoundBrandException;
import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.common.response.RestResponse;
import com.example.musinsasearch.product.dto.request.ProductCreateRequest;
import com.example.musinsasearch.product.dto.request.ProductDeleteRequest;
import com.example.musinsasearch.product.dto.request.ProductUpdateRequest;
import com.example.musinsasearch.product.exception.NotFoundProductException;
import com.example.musinsasearch.product.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductCommandController {
    private final ProductCommandService productCommandService;

    @PostMapping(ProductCommandControllerPath.PRODUCT_SAVE)
    public RestResponse<Void> createProduct(
            @Validated @RequestBody ProductCreateRequest productCreateRequest
    ) {
        productCommandService.createProduct(productCreateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PutMapping(ProductCommandControllerPath.PRODUCT_UPDATE)
    public RestResponse<Void> updateProduct(
            @Validated @RequestBody ProductUpdateRequest productUpdateRequest
    ) {
        productCommandService.updateProduct(productUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @DeleteMapping(ProductCommandControllerPath.PRODUCT_DELETE)
    public RestResponse<Void> deleteProduct(
            @Validated @RequestBody ProductDeleteRequest productDeleteRequest
    ) {
        productCommandService.deleteProduct(productDeleteRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundBrandException.class)
    public RestResponse<Void> notFoundBrandException(NotFoundBrandException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundProductException.class)
    public RestResponse<Void> notFoundProductException(NotFoundProductException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundCategoryException.class)
    public RestResponse<Void> notFoundCategoryException(NotFoundCategoryException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
