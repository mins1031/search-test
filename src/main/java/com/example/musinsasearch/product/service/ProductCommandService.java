package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.exception.NotFoundBrandException;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.request.ProductCreateRequest;
import com.example.musinsasearch.product.dto.request.ProductDeleteRequest;
import com.example.musinsasearch.product.dto.request.ProductUpdateRequest;
import com.example.musinsasearch.product.exception.NotFoundProductException;
import com.example.musinsasearch.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public void createProduct(ProductCreateRequest productCreateRequest) {
        Brand brand = brandRepository.findById(productCreateRequest.getBrandNum()).orElseThrow(NotFoundBrandException::new);
        Category category = categoryRepository.findById(productCreateRequest.getCategoryNum()).orElseThrow(NotFoundCategoryException::new);
        Product product = Product.createProduct(productCreateRequest.getProductName(), productCreateRequest.getProductPrice(), brand, category);

        productRepository.save(product);
    }

    @Transactional
    public void updateProductPrice(ProductUpdateRequest productCreateRequest) {
        Product product = productRepository.findById(productCreateRequest.getProductNum()).orElseThrow(NotFoundProductException::new);
        product.updateProduct(productCreateRequest.getProductName(), productCreateRequest.getProductPrice());
    }

    @Transactional
    public void deleteProductPrice(ProductDeleteRequest productDeleteRequest) {
        Product product = productRepository.findById(productDeleteRequest.getProductNum()).orElseThrow(NotFoundProductException::new);
        productRepository.delete(product);
    }
}
