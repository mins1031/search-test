package com.example.musinsasearch.product.dto.request;

import com.example.musinsasearch.common.validatemessages.RequestValidatorMessages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUpdateRequest {
    @NotNull(message = RequestValidatorMessages.PRODUCT_NUM_NULL)
    private Long productNum;
    @NotBlank(message = RequestValidatorMessages.PRODUCT_NAME_BLANK)
    private String productName;
    @NotNull(message = RequestValidatorMessages.PRODUCT_PRICE_NULL)
    private int productPrice;

    public ProductUpdateRequest(Long productNum, String productName, int productPrice) {
        this.productNum = productNum;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
