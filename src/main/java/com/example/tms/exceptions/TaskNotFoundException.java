package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends ApiException{
    public TaskNotFoundException(HttpStatus status, String title, String description) {
        super(status, title, description);
    }
}