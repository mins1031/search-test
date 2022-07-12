package com.example.musinsasearch.product.service;

import com.example.musinsasearch.brand.domain.Brand;
import com.example.musinsasearch.brand.repository.BrandRepository;
import com.example.musinsasearch.category.domain.Category;
import com.example.musinsasearch.category.exception.NotFoundCategoryException;
import com.example.musinsasearch.category.repository.CategoryRepository;
import com.example.musinsasearch.common.exception.ImpossibleException;
import com.example.musinsasearch.common.validator.RequestAndResultValidator;
import com.example.musinsasearch.product.dto.response.ProductLowestPriceAndBrandResponses;
import com.example.musinsasearch.product.dto.response.ProductPriceAndBrandResponse;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    //모든 카테고의 상품을 브랜드별로 자유롭게 선택해 모든 상품을 구매할때 최저 조회 API
    //흐름! 1) 카테고리별 상품의 최저가와 카테고리정보 조회  ->  2) 카테고리정보와 최저가를 만족하는 상품들을 조회  ->  3) 응답
    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory() {
        List<ProductLowestPriceByCategoryRawDto> productRawDtos = productSearchRepository.findLowestPriceByAllCategory();
        List<ProductCategorizeLowestPriceResponse> productCategorizeLowestPriceResponses = combineProductRawDtos(productRawDtos);
        int totalLowestPriceByCategory = productRawDtos.stream().mapToInt(ProductLowestPriceByCategoryRawDto::getLowestPrice).sum();

        return new ProductCategorizeLowestPriceResponses(productCategorizeLowestPriceResponses, totalLowestPriceByCategory);
    }

    //카테고리와 최저가 정보를 통해 각 카테고리의 최저가 상품 리스트 생성.
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

    // 한 브랜드에 모든 카테고리의 상품 한꺼번에 구매할 경우 최저가 및 브랜드 조회 API
    // 흐름! 1) 각 브랜드의 카테고리별 상품의 최저가 조회 -> 2) 브랜드의 카테고리별 최저가들의 합 계산 -> 3) 각 브랜드 정보와 최저가 합중 최저가 추출 -> 4) 응답
    @Transactional(readOnly = true)
    public ProductLowestPriceAndBrandResponses searchLowestPriceInAllBrand() {
        List<Brand> allBrands = brandRepository.findAll();

        List<ProductLowestPriceAndBrandResponse> productLowestPriceAndBrandResponses = filterLowestPriceFromCategoryAndBrand(allBrands);

        return new ProductLowestPriceAndBrandResponses(productLowestPriceAndBrandResponses);
    }

    //브랜드별 각 카테고리의 최저가를 조회, 더한후 최저가 추출.
    //TODO 기존 코드와 개선 코드의 비교를 위해 기존 코드를 주석처리후 그대로 두었습니다.
    private List<ProductLowestPriceAndBrandResponse> filterLowestPriceFromCategoryAndBrand(List<Brand> brands) {
        List<ProductLowestPriceAndBrandResponse> responses = new ArrayList<>();

        for (Brand brand : brands) {
//            List<Integer> lowestPricesByBrand = searchEachCategoryLowestPrices(categories, brand); // 1)
//            Integer sumPriceByBrand = lowestPricesByBrand.stream().reduce(Integer::sum).get(); // 2)
            Integer sumPriceByBrand = productRepository.sumLowestPriceEachCategoryByBrand(brand.getNum()); // 1), 2)
            ProductLowestPriceAndBrandResponse totalLowestPrice = new ProductLowestPriceAndBrandResponse(brand.getName(), sumPriceByBrand);
            responses.add(totalLowestPrice);
        }

        int lowestPrice = extractLowestPrice(responses);

        return responses.stream()
                .filter(productLowestPriceAndBrandResponse -> productLowestPriceAndBrandResponse.getLowestAllProductSumPrice() == lowestPrice)
                .collect(Collectors.toList());// 3)
    }

    private int extractLowestPrice(List<ProductLowestPriceAndBrandResponse> responses) {
        return responses.stream()
                .min(Comparator.comparing(ProductLowestPriceAndBrandResponse::getLowestAllProductSumPrice))
                .orElseThrow(NoSuchElementException::new).getLowestAllProductSumPrice();
    }

    //브랜드의 각 카테고리별 최저가 조회.
//    private List<Integer> searchEachCategoryLowestPrices(List<Category> categories, Brand brand) {
//        List<Integer> lowestPricesByBrand = new ArrayList<>();
//        for (Category category : categories) {
//            Integer lowestPriceInBrandAndCategory = productSearchRepository.findLowestPriceByCategoryAndBrand(category.getNum(), brand.getNum());
//            if (lowestPriceInBrandAndCategory == null) {
//                continue;
//            }
//
//            lowestPricesByBrand.add(lowestPriceInBrandAndCategory);
//        }
//
//        return lowestPricesByBrand;
//    }

    //각 카테고리 이름으로 최소, 최대 가격조회 API
    //흐름! 1) 카테고리 정보를 토대로 브랜드별 브랜드 정보, 상품의 최대, 최저가 조회 -> 2) 검색된 브랜드별 최고, 최저가중의 최고, 최저가 추출 -> 3) 최대가와 최저가와 동일한 정보를 추출 -> 4) 응답
    @Transactional(readOnly = true)
    public ProductLowestAndHighestPriceResponses searchLowestAndHighestProductByCategory(String categoryName) {
        RequestAndResultValidator.verifyStringParameter(categoryName);
        Category category = categoryRepository.findByName(categoryName).orElseThrow(NotFoundCategoryException::new);
        List<ProductLowestAndHighestPriceRawDto> rawDtos = productSearchRepository.searchLowestPriceAndHighest(category.getNum()); // 1)
        RequestAndResultValidator.verifyEmptyCollection(rawDtos);

        int highestPrice = extractHighestPriceFromRawDtos(rawDtos); // 2)
        int lowestPrice = extractLowestPriceFromRawDtos(rawDtos); // 2)

        return new ProductLowestAndHighestPriceResponses(convertRawDtoToLowestResponse(rawDtos, lowestPrice), convertRawDtoToHighestResponse(rawDtos, highestPrice)); // 4)
    }

    //검색된 브랜드별 최저,최고가 목록에서 최고가 추출
    private int extractHighestPriceFromRawDtos(List<ProductLowestAndHighestPriceRawDto> rawDtos) {
        ProductLowestAndHighestPriceRawDto highestRawDto = rawDtos.stream().max(Comparator.comparing(ProductLowestAndHighestPriceRawDto::getHighestPrice)).orElseThrow(() -> new ImpossibleException("검색한 데이터중 최대값을 구하지 못했습니다."));
        return highestRawDto.getHighestPrice();
    }

    //검색된 브랜드별 최저,최고가 목록에서 최저가 추출
    private int extractLowestPriceFromRawDtos(List<ProductLowestAndHighestPriceRawDto> rawDtos) {
        ProductLowestAndHighestPriceRawDto lowestRawDto = rawDtos.stream().min(Comparator.comparing(ProductLowestAndHighestPriceRawDto::getLowestPrice)).orElseThrow(() -> new ImpossibleException("검색한 데이터중 최소값을 구하지 못했습니다."));
        return lowestRawDto.getLowestPrice();
    }

    //추출된 최저가 와 동일한 데이터를 찾아 응답객체로 변환
    private List<ProductPriceAndBrandResponse> convertRawDtoToLowestResponse(List<ProductLowestAndHighestPriceRawDto> rawDtos, int lowestPrice) {
        List<ProductPriceAndBrandResponse> lowestResponses = rawDtos.stream()
                .filter(rawDto -> rawDto.getLowestPrice() == lowestPrice)
                .map(rawDto -> ProductPriceAndBrandResponse.of(rawDto.getBrandNum(), rawDto.getBrandName(), rawDto.getLowestPrice()))
                .sorted(Comparator.comparing(ProductPriceAndBrandResponse::getBrandNum))
                .collect(Collectors.toList()); // 3)

        return lowestResponses;
    }

    //추출된 최고가 와 동일한 데이터를 찾아 응답객체로 변환
    private List<ProductPriceAndBrandResponse> convertRawDtoToHighestResponse(List<ProductLowestAndHighestPriceRawDto> rawDtos, int highestPrice) {
        List<ProductPriceAndBrandResponse> highestResponses = rawDtos.stream()
                .filter(rawDto -> rawDto.getHighestPrice() == highestPrice)
                .map(rawDto -> ProductPriceAndBrandResponse.of(rawDto.getBrandNum(), rawDto.getBrandName(), rawDto.getHighestPrice()))
                .sorted(Comparator.comparing(ProductPriceAndBrandResponse::getBrandName))
                .collect(Collectors.toList()); // 3)

        return highestResponses;
    }

}
