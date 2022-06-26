package com.example.musinsasearch.common;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.repository.ProductRepository;

public class ProductCreateHelper {

    public static Product 상품_생성(ProductRepository productRepository, String name, int price, Brand brand, Category category) {
        return productRepository.save(Product.createProduct(name, price, brand, category));
    }
}
