package com.example.musinsasearch.product.repository;

import com.example.musinsasearch.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryNum(Long categoryNum);

    @Query(value = "select sum(temp_mins.mins) as total from (select min(price) mins from product where brand_num = ? group by category_num) as temp_mins", nativeQuery = true)
    Integer sumLowestPriceEachCategoryByBrand(Long brandNum);
}
