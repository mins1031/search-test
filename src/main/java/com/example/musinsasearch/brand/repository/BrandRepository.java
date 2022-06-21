package com.example.musinsasearch.brand.repository;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
