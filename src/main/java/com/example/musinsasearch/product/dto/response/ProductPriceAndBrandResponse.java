package com.example.musinsasearch.product.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPriceAndBrandResponse {
    private Long brandNum;
    private String brandName;
    private int productPrice;

    private ProductPriceAndBrandResponse(Long brandNum, String brandName, int productPrice) {
        this.brandNum = brandNum;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }

    public static ProductPriceAndBrandResponse of(Long brandNum, String brandName, int productPrice) {
        return new ProductPriceAndBrandResponse(brandNum, brandName, productPrice);
    }
}
