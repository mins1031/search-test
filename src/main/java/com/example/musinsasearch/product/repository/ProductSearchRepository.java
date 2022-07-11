package com.example.musinsasearch.product.repository;

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
import static com.example.musinsasearch.category.domain.QCategory.category;
import static com.example.musinsasearch.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;

    //카테고리 정보와
    public List<ProductLowestPriceByCategoryRawDto> findLowestPriceByAllCategory() {
        List<ProductLowestPriceByCategoryRawDto> productRawDtos = jpaQueryFactory.select(
                        new QProductLowestPriceByCategoryRawDto(category.num, category.name, product.price.min())
                )
                .from(product)
                .innerJoin(product.category, category)
                .groupBy(product.category)
                .fetch();
        return productRawDtos;
    }

    public List<ProductBrandNumAndNameRawDto> findLowestPriceAndBrandByCategory(int lowestPrice, Long categoryNum) {
        List<ProductBrandNumAndNameRawDto> productRawDtos = jpaQueryFactory.select(
                        new QProductBrandNumAndNameRawDto(product.num, brand.num, brand.name)
                )
                .from(product)
                .innerJoin(product.brand, brand)
                .where(product.price.eq(lowestPrice).and(product.category.num.eq(categoryNum)))
                .fetch();

        return productRawDtos;
    }

    public Integer findLowestPriceByCategoryAndBrand(Long categoryNum, Long brandNum) {
        Integer result = jpaQueryFactory.select(product.price.min())
                .from(product)
                .where(product.category.num.eq(categoryNum).and(product.brand.num.eq(brandNum)))
                .fetchFirst();

        return result;
    }

    //카테고리에 맞는 브랜드별 최저,최고가 및 브랜드 정보 조회
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
