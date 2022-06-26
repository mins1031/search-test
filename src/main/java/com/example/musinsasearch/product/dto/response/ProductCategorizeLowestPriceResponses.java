package com.example.musinsasearch.product.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategorizeLowestPriceResponses {
    private List<ProductCategorizeLowestPriceResponse> ProductCategorizeLowestPriceResponses;

    public ProductCategorizeLowestPriceResponses(List<ProductCategorizeLowestPriceResponse> productCategorizeLowestPriceResponses) {
        ProductCategorizeLowestPriceResponses = productCategorizeLowestPriceResponses;
    }
}
