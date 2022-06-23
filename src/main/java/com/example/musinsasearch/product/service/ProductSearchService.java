package com.example.musinsasearch.product.service;

import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.repository.ProductRepository;
import com.example.musinsasearch.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory() {
        List<Product> products = productRepository.findAll();
        return null;
    }
}
