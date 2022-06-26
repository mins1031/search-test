package com.example.musinsasearch.product.repository;

import com.example.musinsasearch.category.domain.QCategory;
import com.example.musinsasearch.product.dto.raw.ProductBrandNumAndNameRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestAndHighestPriceRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestPriceByCategoryRawDto;
import com.example.musinsasearch.product.dto.raw.QProductBrandNumAndNameRawDto;
import com.example.musinsasearch.product.dto.raw.QProductLowestAndHighestPriceRawDto;
import com.example.musinsasearch.product.dto.raw.QProductLowestPriceByCategoryRawDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.musinsasearch.brand.domain.QBrand.brand;
import static com.example.musinsasearch.category.domain.QCategory.*;
import static com.example.musinsasearch.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Integer> searchProductLowestPricesByCategory() {
        List<Integer> products = jpaQueryFactory.select(product.price.min())
                .from(product)
                .groupBy(product.category)
                .fetch();

        return products;
    }

    public List<ProductLowestPriceByCategoryRawDto> searchProductLowestPricesByCategory2() {
        List<ProductLowestPriceByCategoryRawDto> productRawDtos = jpaQueryFactory.select(
                        new QProductLowestPriceByCategoryRawDto(category.num, category.name, product.price.min())
                )
                .from(product)
                .innerJoin(product.category, category)
                .groupBy(product.category)
                .fetch();

        return productRawDtos;
    }

    public List<ProductBrandNumAndNameRawDto> searchProductLowestPricesByCategory3(int lowestPrice, Long categoryNum) {
        List<ProductBrandNumAndNameRawDto> productRawDtos = jpaQueryFactory.select(
                        new QProductBrandNumAndNameRawDto(product.num, brand.num, brand.name)
                )
                .from(product)
                .innerJoin(product.brand, brand)
                .where(product.price.eq(lowestPrice).and(product.category.num.eq(categoryNum)))
                .fetch();

        return productRawDtos;
    }

//////api 1 끝////////

    public List<Integer> findLowestPriceByCategoryAndBrand(Long categoryNum, Long brandNum) {
        List<Integer> results = jpaQueryFactory.select(product.price.min())
                .from(product)
                .distinct()
                .where(product.category.num.eq(categoryNum).and(product.brand.num.eq(brandNum)))
                .fetch();

        return results;
    }

    public List<ProductLowestAndHighestPriceRawDto> searchLowestPriceAndHighest(Long categoryNum) {
        List<ProductLowestAndHighestPriceRawDto> results = jpaQueryFactory.select(
                        new QProductLowestAndHighestPriceRawDto(product.brand.num, product.brand.name, product.price.min(), product.price.max())
                )
                .from(product)
                .innerJoin(product.brand, brand)
                .where(product.category.num.eq(categoryNum))
                .groupBy(product.brand)
                .fetch();

        return results;
    }


}
