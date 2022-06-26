package com.example.musinsasearch.product.controller;

import com.example.musinsasearch.common.response.RestResponse;
import com.example.musinsasearch.product.dto.request.ProductCreateRequest;
import com.example.musinsasearch.product.dto.request.ProductDeleteRequest;
import com.example.musinsasearch.product.dto.request.ProductUpdateRequest;
import com.example.musinsasearch.product.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PatchMapping(ProductCommandControllerPath.PRODUCT_SAVE)
    public RestResponse<Void> updateProduct(
            @Validated @RequestBody ProductUpdateRequest productUpdateRequest
    ) {
        productCommandService.updateProduct(productUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PostMapping(ProductCommandControllerPath.PRODUCT_SAVE)
    public RestResponse<Void> deleteProduct(
            @Validated @RequestBody ProductDeleteRequest productDeleteRequest
    ) {
        productCommandService.deleteProduct(productDeleteRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
