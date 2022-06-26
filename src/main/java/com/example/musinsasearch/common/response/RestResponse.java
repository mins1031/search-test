package com.example.musinsasearch.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestResponse<T> {
    private HttpStatus statusCode;
    private String message;
    private T result;

    private RestResponse(HttpStatus statusCode, String message, T result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public static <Void> RestResponse<Void> error(HttpStatus code, String message) {
        return new RestResponse<>(code, message, null);
    }

    public static <T> RestResponse<T> of(HttpStatus statusCode, T result) {
        return new RestResponse<T>(statusCode, null, result);
    }
}
