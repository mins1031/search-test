package com.example.musinsasearch.product.dto.raw;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductBrandNumAndNameRawDto {
    private Long productNum;
    private Long brandNum;
    private String brandName;

    @QueryProjection
    public ProductBrandNumAndNameRawDto(Long productNum, Long brandNum, String brandName) {
        this.productNum = productNum;
        this.brandNum = brandNum;
        this.brandName = brandName;
    }
}
