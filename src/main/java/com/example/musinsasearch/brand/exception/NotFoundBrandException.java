package com.example.musinsasearch.brand.exception;

public class NotFoundBrandException extends RuntimeException {
    private static final String MESSAGE = "브랜드 정보를 찾을수 없습니다. 요청을 확인해 주세요.";

    public NotFoundBrandException() {
        super(MESSAGE);
    }
}
