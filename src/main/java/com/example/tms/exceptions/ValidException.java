package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

public class ValidException extends ApiException {
    public ValidException(HttpStatus status, String title, String description) {
        super(status, title, description);
    }
}
