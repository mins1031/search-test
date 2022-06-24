package com.example.musinsasearch.common.validator;

import com.example.musinsasearch.common.exception.SearchResultEmptyException;

import java.util.Collection;
import java.util.List;

public class RequestAndResultValidator {

    public static <T> void verifyEmptyCollection(Collection<T> list) {
        if (list.isEmpty()) {
            throw new SearchResultEmptyException();
        }
    }

    public static <T> void verifyStringParameter(String parameter) {
        if (parameter == null || parameter.isEmpty() || parameter.isBlank()) {
            throw new SearchResultEmptyException();
        }
    }
}
