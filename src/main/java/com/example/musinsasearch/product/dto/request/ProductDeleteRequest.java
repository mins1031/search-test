package com.example.musinsasearch.product.dto.request;

import com.example.musinsasearch.common.validatemessages.RequestValidatorMessages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDeleteRequest {
    @NotNull(message = RequestValidatorMessages.PRODUCT_NUM_NULL)
    private Long productNum;

    public ProductDeleteRequest(Long productNum) {
        this.productNum = productNum;
    }
}
