package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

public class StatusNotFoundException extends ApiException {
    public StatusNotFoundException(HttpStatus status, String title, String description) {
        super(status, title, description);
    }
}
