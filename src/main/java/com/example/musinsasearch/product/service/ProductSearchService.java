package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.exception.NotExistCategoryException;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.exception.ImpossibleException;
import com.example.musinsasearch.common.exception.SearchResultEmptyException;
import com.example.musinsasearch.common.validator.RequestAndResultValidator;
import com.example.musinsasearch.product.domain.Product;
import com.example.musinsasearch.product.dto.ProductPriceAndBrandResponse;
import com.example.musinsasearch.product.dto.raw.ProductLowestAndHighestPriceRawDto;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponse;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestAndHighestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponse;
import com.example.musinsasearch.product.repository.ProductRepository;
import com.example.musinsasearch.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory() {
        List<Category> allCategories = categoryRepository.findAll();
        //find category num 만 가져올수는 없나? 되면 얼마나 걸리나..?
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

//    @Transactional(readOnly = true)
//    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory2() {
//        long startTime = System.currentTimeMillis();
//
//        List<Product> allProducts = productRepository.findAll();
//        Map<Category, List<Product>> categoryListMap = allProducts.stream().collect(Collectors.groupingBy(Product::getCategory));
//
//        List<Product> productsGroupByCategory = categoryListMap.values().stream()
//                .map(values -> values.stream().min(Comparator.comparing(Product::getPrice)).orElse(null))
//                .sorted(Comparator.comparing(product -> product.getCategory().getNum()))
//                .collect(Collectors.toList());
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("걸린 시간: " + (endTime - startTime));
//
//        return new ProductCategorizeLowestPriceResponses(ProductCategorizeLowestPriceResponse.listOf(productsGroupByCategory));
//    }

    @Transactional(readOnly = true)
    public ProductLowestPriceAndBrandResponse searchLowestPriceInAllBrand() {
        List<Brand> brands = brandRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        ProductLowestPriceAndBrandResponse productLowestPriceAndBrandResponse = filterLowestPriceFromCategoryAndBrand(brands, categories);

        return productLowestPriceAndBrandResponse;
    }

    private ProductLowestPriceAndBrandResponse filterLowestPriceFromCategoryAndBrand(List<Brand> brands, List<Category> categories) {
        List<ProductLowestPriceAndBrandResponse> responses = new ArrayList<>();

        for (Brand brand : brands) {
            List<Integer> lowestPricesByBrand = searchEachCategoryLowestPrices(categories, brand);
            Integer sumPriceByBrand = lowestPricesByBrand.stream().reduce(Integer::sum).get();
            ProductLowestPriceAndBrandResponse totalLowestPrice = new ProductLowestPriceAndBrandResponse(brand.getName(), sumPriceByBrand);
            responses.add(totalLowestPrice);
        }

        return responses.stream().min(Comparator.comparing(ProductLowestPriceAndBrandResponse::getLowestAllProductSumPrice)).get();
    }

    private List<Integer> searchEachCategoryLowestPrices(List<Category> categories, Brand brand) {
        List<Integer> lowestPricesByBrand = new ArrayList<>();
        for (Category category : categories) {
            List<Integer> productLowestPriceAndBrandResponses = productSearchRepository.findLowestPriceByCategoryAndBrand(category.getNum(), brand.getNum());
            if (productLowestPriceAndBrandResponses.size() == 0) {
                continue;
            }

            lowestPricesByBrand.add(productLowestPriceAndBrandResponses.get(0));
        }

        return lowestPricesByBrand;
    }

    @Transactional(readOnly = true)
    public ProductLowestAndHighestPriceResponses searchLowestAndHighestProductByCategory(String categoryName) {
        RequestAndResultValidator.verifyStringParameter(categoryName);
        Category category = categoryRepository.findByName(categoryName).orElseThrow(NotExistCategoryException::new);
        List<ProductLowestAndHighestPriceRawDto> rawDtos = productSearchRepository.searchLowestPriceAndHighest(category.getNum());
        RequestAndResultValidator.verifyEmptyCollection(rawDtos);

        int maxPrice = extractHighestPriceFromRawDtos(rawDtos);
        int minPrice = extractLowestPriceFromRawDtos(rawDtos);

        return new ProductLowestAndHighestPriceResponses(convertRawDtoToLowestResponse(rawDtos, minPrice), convertRawDtoToHighestResponse(rawDtos, maxPrice));
    }

    private int extractHighestPriceFromRawDtos(List<ProductLowestAndHighestPriceRawDto> rawDtos) {
        ProductLowestAndHighestPriceRawDto highestRawDto = rawDtos.stream().max(Comparator.comparing(ProductLowestAndHighestPriceRawDto::getHighestPrice)).orElseThrow(() -> new ImpossibleException("검색한 데이터중 최대값을 구하지 못했습니다."));
        return highestRawDto.getHighestPrice();
    }

    private int extractLowestPriceFromRawDtos(List<ProductLowestAndHighestPriceRawDto> rawDtos) {
        ProductLowestAndHighestPriceRawDto lowestRawDto = rawDtos.stream().min(Comparator.comparing(ProductLowestAndHighestPriceRawDto::getLowestPrice)).orElseThrow(() -> new ImpossibleException("검색한 데이터중 최소값을 구하지 못했습니다."));
        return lowestRawDto.getLowestPrice();
    }

    private List<ProductPriceAndBrandResponse> convertRawDtoToLowestResponse(List<ProductLowestAndHighestPriceRawDto> rawDtos, int lowestPrice) {
        List<ProductPriceAndBrandResponse> lowestResponses = rawDtos.stream()
                .filter(rawDto -> rawDto.getLowestPrice() == lowestPrice)
                .map(rawDto -> ProductPriceAndBrandResponse.of(rawDto.getBrandNum(), rawDto.getBrandName(), rawDto.getLowestPrice()))
                .collect(Collectors.toList());

        return lowestResponses;
    }

    private List<ProductPriceAndBrandResponse> convertRawDtoToHighestResponse(List<ProductLowestAndHighestPriceRawDto> rawDtos, int highestPrice) {
        List<ProductPriceAndBrandResponse> highestResponses = rawDtos.stream()
                .filter(rawDto -> rawDto.getHighestPrice() == highestPrice)
                .map(rawDto -> ProductPriceAndBrandResponse.of(rawDto.getBrandNum(), rawDto.getBrandName(), rawDto.getHighestPrice()))
                .collect(Collectors.toList());

        return highestResponses;
    }

}
