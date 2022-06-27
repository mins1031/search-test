package com.example.musinsasearch.product.dto.request;

import com.example.musinsasearch.common.validatemessages.RequestValidatorMessages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCreateRequest {
    @NotBlank(message = RequestValidatorMessages.PRODUCT_NAME_BLANK)
    private String productName;
    @NotNull(message = RequestValidatorMessages.PRODUCT_PRICE_NULL)
    private int productPrice;
    @NotNull(message = RequestValidatorMessages.BRAND_NUM_NULL)
    private Long brandNum;
    @NotNull(message = RequestValidatorMessages.CATEGORY_NUM_NULL)
    private Long categoryNum;

    public ProductCreateRequest(String productName, int productPrice, Long brandNum, Long categoryNum) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.brandNum = brandNum;
        this.categoryNum = categoryNum;
    }
}
