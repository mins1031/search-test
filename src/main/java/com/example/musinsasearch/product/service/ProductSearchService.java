package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.exception.ImpossibleException;
import com.example.musinsasearch.common.validator.RequestAndResultValidator;
import com.example.musinsasearch.product.dto.response.ProductPriceAndBrandResponse;
import com.example.musinsasearch.product.dto.raw.ProductBrandNumAndNameRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestAndHighestPriceRawDto;
import com.example.musinsasearch.product.dto.raw.ProductLowestPriceByCategoryRawDto;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponse;
import com.example.musinsasearch.product.dto.response.ProductCategorizeLowestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestAndHighestPriceResponses;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponse;
import com.example.musinsasearch.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    //
    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory() {
        List<ProductLowestPriceByCategoryRawDto> productRawDtos = productSearchRepository.findLowestPriceByAllCategory();
        List<ProductCategorizeLowestPriceResponse> productCategorizeLowestPriceResponses = combineProductRawDtos(productRawDtos);

        return new ProductCategorizeLowestPriceResponses(productCategorizeLowestPriceResponses);
    }

    private List<ProductCategorizeLowestPriceResponse> combineProductRawDtos(List<ProductLowestPriceByCategoryRawDto> productRawDtos) {
        List<ProductCategorizeLowestPriceResponse> productResponses = new ArrayList<>();

        for (ProductLowestPriceByCategoryRawDto productRawDto : productRawDtos) {
            List<ProductBrandNumAndNameRawDto> productBrandNumAndNameRawDtos = productSearchRepository.findLowestPriceAndBrandByCategory(productRawDto.getLowestPrice(), productRawDto.getCategoryNum());
            productResponses.addAll(productBrandNumAndNameRawDtos.stream()
                    .map(rawDto -> ProductCategorizeLowestPriceResponse.of(productRawDto, rawDto))
                    .collect(Collectors.toList()));
        }

        return productResponses;
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
            Integer lowestPriceInBrandAndCategory = productSearchRepository.findLowestPriceByCategoryAndBrand(category.getNum(), brand.getNum());
            if (lowestPriceInBrandAndCategory == null) {
                continue;
            }

            lowestPricesByBrand.add(lowestPriceInBrandAndCategory);
        }

        return lowestPricesByBrand;
    }

    @Transactional(readOnly = true)
    public ProductLowestAndHighestPriceResponses searchLowestAndHighestProductByCategory(String categoryName) {
        RequestAndResultValidator.verifyStringParameter(categoryName);
        Category category = categoryRepository.findByName(categoryName).orElseThrow(NotFoundCategoryException::new);
        List<ProductLowestAndHighestPriceRawDto> rawDtos = productSearchRepository.searchLowestPriceAndHighest(category.getNum());
        RequestAndResultValidator.verifyEmptyCollection(rawDtos);

        int highestPrice = extractHighestPriceFromRawDtos(rawDtos);
        int lowestPrice = extractLowestPriceFromRawDtos(rawDtos);

        return new ProductLowestAndHighestPriceResponses(convertRawDtoToLowestResponse(rawDtos, lowestPrice), convertRawDtoToHighestResponse(rawDtos, highestPrice));
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
