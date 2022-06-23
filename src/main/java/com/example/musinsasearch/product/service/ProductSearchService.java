package com.example.musinsasearch.product.service;

import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.ProductCategorizeLowestPriceResponse;
import com.example.musinsasearch.product.dto.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.repository.ProductRepository;
import com.example.musinsasearch.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory() {
        List<Category> allCategories = categoryRepository.findAll();
        List<Product> productsByLowestPrice = new ArrayList<>();

        for (Category category : allCategories) {
            List<Product> productsByCategory = productRepository.findByCategoryNum(category.getNum());
            productsByLowestPrice.add(productsByCategory.stream().min(Comparator.comparing(Product::getPrice)).orElse(null));
        }

//        List<Product> allProducts = productRepository.findAll();
//        List<ProductCategorizeLowestPriceResponse> productCategorizeLowestPriceResponses = productSearchRepository.searchProductLowestPricesByCategory();
////        Map<String, List<ProductCategorizeLowestPriceResponse>> categoryListMap = productCategorizeLowestPriceResponses.stream().collect(Collectors.groupingBy(ProductCategorizeLowestPriceResponse::getCategoryName));
//        List<ProductCategorizeLowestPriceResponse> productsGroupByCategory = categoryListMap.values().stream()
//                .map(values -> values.stream().min(Comparator.comparing(ProductCategorizeLowestPriceResponse::getPrice)).orElse(null))
//                .sorted(Comparator.comparing(product -> product.getCategoryName()))
//                .collect(Collectors.toList());

        return new ProductCategorizeLowestPriceResponses(ProductCategorizeLowestPriceResponse.listOf(productsByLowestPrice));
    }

    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory2() {
        long startTime = System.currentTimeMillis();

        List<Product> allProducts = productRepository.findAll();
        Map<Category, List<Product>> categoryListMap = allProducts.stream().collect(Collectors.groupingBy(Product::getCategory));

        List<Product> productsGroupByCategory = categoryListMap.values().stream()
                .map(values -> values.stream().min(Comparator.comparing(Product::getPrice)).orElse(null))
                .sorted(Comparator.comparing(product -> product.getCategory().getNum()))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (endTime - startTime));

        return new ProductCategorizeLowestPriceResponses(ProductCategorizeLowestPriceResponse.listOf(productsGroupByCategory));
    }
}
