package com.example.musinsasearch.product.dto.raw;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLowestAndHighestPriceRawDto {
    private Long brandNum;
    private String brandName;
    private int lowestPrice;
    private int highestPrice;

    @QueryProjection
    public ProductLowestAndHighestPriceRawDto(Long brandNum, String brandName, int lowestPrice, int highestPrice) {
        this.brandNum = brandNum;
        this.brandName = brandName;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
    }
}
