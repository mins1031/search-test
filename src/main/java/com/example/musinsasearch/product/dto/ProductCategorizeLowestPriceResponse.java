package com.example.musinsasearch.product.dto;

import com.example.musinsasearch.product.domain.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategorizeLowestPriceResponse {
    private Long productNum;
    private String productName;
    private String categoryName;
    private String brandName;
    private int price;

    @QueryProjection
    public ProductCategorizeLowestPriceResponse(Long productNum, String productName, String categoryName, String brandName, int price) {
        this.productNum = productNum;
        this.productName = productName;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.price = price;
    }

    public static ProductCategorizeLowestPriceResponse of(Product product) {
        return new ProductCategorizeLowestPriceResponse(
                product.getNum(),
                product.getName(),
                product.getCategory().getName(),
                product.getBrand().getName(),
                product.getPrice()
        );
    }

    public static List<ProductCategorizeLowestPriceResponse> listOf(List<Product> products) {
        return products.stream()
                .map(ProductCategorizeLowestPriceResponse::of)
                .collect(Collectors.toList());
    }
}
