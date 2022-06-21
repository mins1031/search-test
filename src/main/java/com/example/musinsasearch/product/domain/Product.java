package com.example.musinsasearch.product.domain;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BasicEntity {

    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Product(String name, int price, Brand brand, Category category) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.category = category;
    }

    public static Product createProduct(String name, int price, Brand brand, Category category) {
        return new Product(name, price, brand, category);
    }
}
