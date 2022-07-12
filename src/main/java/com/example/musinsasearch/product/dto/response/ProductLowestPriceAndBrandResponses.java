package com.example.musinsasearch.product.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLowestPriceAndBrandResponses {
    private List<ProductLowestPriceAndBrandResponse> productLowestPriceAndBrandResponses;

    public ProductLowestPriceAndBrandResponses(List<ProductLowestPriceAndBrandResponse> productLowestPriceAndBrandResponses) {
        this.productLowestPriceAndBrandResponses = productLowestPriceAndBrandResponses;
    }
}
