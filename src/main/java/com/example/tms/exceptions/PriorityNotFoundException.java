package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

public class PriorityNotFoundException extends ApiException {
    public PriorityNotFoundException(HttpStatus status, String title, String description) {
        super(status, title, description);
    }
}
