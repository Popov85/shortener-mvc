package com.shortener.shortenermvc.web.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                (ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.")
        );
        problemDetail.setProperty("error", ex.getClass().getSimpleName());
        return problemDetail;
    }

}
