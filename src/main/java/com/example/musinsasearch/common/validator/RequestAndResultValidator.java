package com.example.musinsasearch.common.validator;

import com.example.musinsasearch.common.exception.SearchResultEmptyException;
import com.example.musinsasearch.common.exception.WrongParameterException;

import java.util.Collection;

public class RequestAndResultValidator {

    public static <T> void verifyEmptyCollection(Collection<T> list) {
        if (list.isEmpty()) {
            throw new SearchResultEmptyException();
        }
    }

    public static void verifyStringParameter(String parameter) {
        if (parameter == null || parameter.isEmpty() || parameter.isBlank()) {
            throw new WrongParameterException("카테고리 이름이 부적절합니다. 요청을 확인해 주세요.");
        }
    }
}
