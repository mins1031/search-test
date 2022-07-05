package com.example.musinsasearch.common.exceptionhandler;

import com.example.musinsasearch.common.exception.ImpossibleException;
import com.example.musinsasearch.common.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class AdviceExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResponse<Void> Exception(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러 입니다. 고객센터로 문의 해주세요.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ImpossibleException.class})
    public RestResponse<Void> impossibleException(ImpossibleException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public RestResponse<Void> nullPointerException(NullPointerException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러 입니다. 고객센터로 문의 해주세요.");
    }
}
