package com.example.musinsasearch.product.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCreateRequest {
    private String productName;
    private int productPrice;
    private Long brandNum;
    private Long categoryNum;

    public ProductCreateRequest(String productName, int productPrice, Long brandNum, Long categoryNum) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.brandNum = brandNum;
        this.categoryNum = categoryNum;
    }
}
