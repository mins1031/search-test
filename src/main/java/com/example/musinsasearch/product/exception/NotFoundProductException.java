package com.example.musinsasearch.product.exception;

public class NotFoundProductException extends RuntimeException {
    private static final String MESSAGE = "상품을 찾을수 없습니다. 요청을 다시 확인해 주세요.";

    public NotFoundProductException() {
        super(MESSAGE);
    }
}
