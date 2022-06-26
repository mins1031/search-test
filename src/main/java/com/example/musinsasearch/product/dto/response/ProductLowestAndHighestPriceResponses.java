package com.example.musinsasearch.product.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLowestAndHighestPriceResponses {
    private List<ProductPriceAndBrandResponse> lowestResponses;
    private List<ProductPriceAndBrandResponse> highestResponses;

    public ProductLowestAndHighestPriceResponses(List<ProductPriceAndBrandResponse> lowestResponses, List<ProductPriceAndBrandResponse> highestResponses) {
        this.lowestResponses = lowestResponses;
        this.highestResponses = highestResponses;
    }
}
