package com.example.musinsasearch.product.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDeleteRequest {
    private Long productNum;

    public ProductDeleteRequest(Long productNum) {
        this.productNum = productNum;
    }
}
