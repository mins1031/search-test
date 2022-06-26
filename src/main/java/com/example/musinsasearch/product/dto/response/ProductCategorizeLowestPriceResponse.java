package com.example.musinsasearch.product.dto.response;

import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.raw.ProductBrandNumAndNameRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestPriceByCategoryRawDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategorizeLowestPriceResponse {
    private Long productNum;
    private Long categoryNum;
    private String categoryName;
    private Long brandNum;
    private String brandName;
    private int price;

    private ProductCategorizeLowestPriceResponse(ProductLowestPriceByCategoryRawDto productLowestPriceByCategoryRawDto, ProductBrandNumAndNameRawDto productBrandNumAndNameRawDto) {
        this.productNum = productBrandNumAndNameRawDto.getProductNum();
        this.categoryNum = productLowestPriceByCategoryRawDto.getCategoryNum();
        this.categoryName = productLowestPriceByCategoryRawDto.getCategoryName();
        this.brandNum = productBrandNumAndNameRawDto.getBrandNum();
        this.brandName = productBrandNumAndNameRawDto.getBrandName();
        this.price = productLowestPriceByCategoryRawDto.getLowestPrice();
    }

    public static ProductCategorizeLowestPriceResponse of(ProductLowestPriceByCategoryRawDto productLowestPriceByCategoryRawDto, ProductBrandNumAndNameRawDto productBrandNumAndNameRawDto) {
        return new ProductCategorizeLowestPriceResponse(productLowestPriceByCategoryRawDto, productBrandNumAndNameRawDto);
    }
}
