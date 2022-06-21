package com.example.musinsasearch.product.repository;

import com.example.musinsasearch.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
