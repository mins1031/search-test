package com.example.musinsasearch.common.exception;

public class SearchResultEmptyException extends RuntimeException {
    private static final String MESSAGE = "검색한 데이터가 없습니다.";

    public SearchResultEmptyException() {
        super(MESSAGE);
    }
}
