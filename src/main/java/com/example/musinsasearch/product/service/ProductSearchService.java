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

    //모든 카테고의 상품을 브랜드별로 자유롭 선택해 모든 상품을 구매할때 최저 조회 API
    @Transactional(readOnly = true)
    public ProductCategorizeLowestPriceResponses searchProductLowestPricesByCategory() {
        List<ProductLowestPriceByCategoryRawDto> productRawDtos = productSearchRepository.findLowestPriceByAllCategory();
        List<ProductCategorizeLowestPriceResponse> productCategorizeLowestPriceResponses = combineProductRawDtos(productRawDtos);

        return new ProductCategorizeLowestPriceResponses(productCategorizeLowestPriceResponses);
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

    //한 브랜드에 모든 카테고리의 상품 한꺼번에 구매할 경우 최저가 및 브랜드 조회 API
    @Transactional(readOnly = true)
    public ProductLowestPriceAndBrandResponse searchLowestPriceInAllBrand() {
        List<Brand> brands = brandRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        ProductLowestPriceAndBrandResponse productLowestPriceAndBrandResponse = filterLowestPriceFromCategoryAndBrand(brands, categories);

        return productLowestPriceAndBrandResponse;
    }

    //브랜드별 각 카테고리의 최저가를 조회, 더한후 최저가 추출.
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

    //브랜드의 각 카테고리별 최저가 조회.
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

    //각 카테고리 이름으로 최소, 최대 가격조회 API
    @Transactional(readOnly = true)
    public ProductLowestAndHighestPriceResponses searchLowestAndHighestProductByCategory(String categoryName) {
        RequestAndResultValidator.verifyStringParameter(categoryName);
        Category category = categoryRepository.findByName(categoryName).orElseThrow(NotFoundCategoryException::new);
        //카테고리에 맞는 브랜드별 최저,최고가 및 브랜드 정보 조회
        List<ProductLowestAndHighestPriceRawDto> rawDtos = productSearchRepository.searchLowestPriceAndHighest(category.getNum());
        RequestAndResultValidator.verifyEmptyCollection(rawDtos);

        int highestPrice = extractHighestPriceFromRawDtos(rawDtos);
        int lowestPrice = extractLowestPriceFromRawDtos(rawDtos);

        return new ProductLowestAndHighestPriceResponses(convertRawDtoToLowestResponse(rawDtos, lowestPrice), convertRawDtoToHighestResponse(rawDtos, highestPrice));
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
                .collect(Collectors.toList());

        return lowestResponses;
    }

    //추출된 최고가 와 동일한 데이터를 찾아 응답객체로 변환
    private List<ProductPriceAndBrandResponse> convertRawDtoToHighestResponse(List<ProductLowestAndHighestPriceRawDto> rawDtos, int highestPrice) {
        List<ProductPriceAndBrandResponse> highestResponses = rawDtos.stream()
                .filter(rawDto -> rawDto.getHighestPrice() == highestPrice)
                .map(rawDto -> ProductPriceAndBrandResponse.of(rawDto.getBrandNum(), rawDto.getBrandName(), rawDto.getHighestPrice()))
                .collect(Collectors.toList());

        return highestResponses;
    }

}
