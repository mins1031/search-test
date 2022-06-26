package com.example.musinsasearch.product.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUpdateRequest {
    private Long productNum;
    private String productName;
    private int productPrice;

    public ProductUpdateRequest(Long productNum, String productName, int productPrice) {
        this.productNum = productNum;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
