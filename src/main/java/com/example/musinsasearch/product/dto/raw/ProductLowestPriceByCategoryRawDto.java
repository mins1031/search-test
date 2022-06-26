package com.example.musinsasearch.product.dto.raw;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLowestPriceByCategoryRawDto {
    private Long categoryNum;
    private String categoryName;
    private int lowestPrice;

    @QueryProjection
    public ProductLowestPriceByCategoryRawDto(Long categoryNum, String categoryName, int lowestPrice) {
        this.categoryNum = categoryNum;
        this.categoryName = categoryName;
        this.lowestPrice = lowestPrice;
    }
}
