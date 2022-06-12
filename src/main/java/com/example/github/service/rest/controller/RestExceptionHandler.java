package com.example.github.service.rest.controller;

import org.junit.jupiter.api.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.github.service.dto.ErrorResponseDTO;

import feign.FeignException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, error));
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponseDTO errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    protected ResponseEntity<Object> handleUnauthorized(FeignException.Unauthorized ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.status(), HttpStatus.valueOf(ex.status()), ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(FeignException.Forbidden.class)
    protected ResponseEntity<Object> handleForbidden(FeignException.Forbidden ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.status(), HttpStatus.valueOf(ex.status()), ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    protected ResponseEntity<Object> handleNotFound(FeignException.NotFound ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.status(), HttpStatus.valueOf(ex.status()), ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

}