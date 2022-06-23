package com.example.musinsasearch.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLowestPriceAndBrandResponse {
    private String lowestBrandName;
    private int lowestAllProductSumPrice;

    @QueryProjection
    public ProductLowestPriceAndBrandResponse(String lowestBrandName, int lowestAllProductSumPrice) {
        this.lowestBrandName = lowestBrandName;
        this.lowestAllProductSumPrice = lowestAllProductSumPrice;
    }
}
