package com.example.musinsasearch.product.domain;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.common.domain.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_brand_category", columnList = "brand_num, category_num"),
        @Index(name = "idx_category", columnList = "category_num")
})
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

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
