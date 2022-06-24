package com.example.musinsasearch.product.repository;

import com.example.musinsasearch.brand.domain.QBrand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.musinsasearch.brand.domain.QBrand.brand;
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

    public List<Integer> findByProductPriceByCategory(Long categoryNum) {
        List<Integer> products = jpaQueryFactory.select(product.price.min())
                .from(product)
                .where(product.category.num.eq(categoryNum))
                .fetch();

        return products;
    }

    public List<Integer> findLowestPriceByCategoryAndBrand(Long categoryNum, Long brandNum) {
        List<Integer> results = jpaQueryFactory.select(product.price.min())
                .from(product)
                .distinct()
                .where(product.category.num.eq(categoryNum).and(product.brand.num.eq(brandNum)))
                .fetch();

        return results;
    }

    public List<Integer> searchLowestPriceAndHighest(Long categoryNum) {
        jpaQueryFactory.select(product.brand.num, product.brand.name, product.price.max(), product.price.min())
                .from(product)
                .innerJoin(product.brand, brand).fetchJoin()
                .where(product.category.num.eq(categoryNum))
                .groupBy(product.brand)
                .fetch();

        return results;
    }





}
