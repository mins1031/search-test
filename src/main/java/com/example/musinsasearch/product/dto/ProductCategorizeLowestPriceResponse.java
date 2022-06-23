package com.example.musinsasearch.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategorizeLowestPriceResponse {
    private Long productNum;
    private String productName;
    private String categoryName;
    private String brandName;

    public ProductCategorizeLowestPriceResponse(Long productNum, String productName, String categoryName, String brandName) {
        this.productNum = productNum;
        this.productName = productName;
        this.categoryName = categoryName;
        this.brandName = brandName;
    }
}
