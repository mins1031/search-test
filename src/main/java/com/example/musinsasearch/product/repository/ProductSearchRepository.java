package com.example.musinsasearch.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.musinsasearch.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Integer> findByProductPricesByCategory() {
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
}
