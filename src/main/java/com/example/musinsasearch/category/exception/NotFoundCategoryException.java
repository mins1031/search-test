package com.example.musinsasearch.category.exception;

public class NotFoundCategoryException extends RuntimeException {
    private static final String MESSAGE = "카테고리 조회에 실패 했습니다. 카테고리 정보를 확인해 주세요.";

    public NotFoundCategoryException() {
        super(MESSAGE);
    }
}