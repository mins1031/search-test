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
import com.example.musinsasearch.product.dto.raw.ProductBrandNumAndNameRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestAndHighestPriceRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestPriceByCategoryRawDto;
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
        //다 때려박고 정렬을 브랜드 순으로 할것.
        List<ProductCategorizeLowestPriceResponse> responses = new ArrayList<>();

        List<ProductLowestPriceByCategoryRawDto> productRawDtos = productSearchRepository.searchProductLowestPricesByCategory2();
        for (ProductLowestPriceByCategoryRawDto productRawDto : productRawDtos) {
            List<ProductBrandNumAndNameRawDto> productBrandNumAndNameRawDtos = productSearchRepository.searchProductLowestPricesByCategory3(productRawDto.getLowestPrice(), productRawDto.getCategoryNum());
            responses.addAll(productBrandNumAndNameRawDtos.stream()
                    .map(rawDto -> ProductCategorizeLowestPriceResponse.of(productRawDto, rawDto))
                    .collect(Collectors.toList()));
        }

        return new ProductCategorizeLowestPriceResponses(responses);
    }

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
