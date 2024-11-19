package com.example.tms.exceptions.handlers;


import com.example.tms.dto.ApiErrorResponse;
import com.example.tms.exceptions.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiErrorResponse> handler(ApiException exception) {
        return new ResponseEntity<>(new ApiErrorResponse(exception.getTitle(), exception.getDescription(), exception.getTime())
                , exception.getStatus());
    }
}
