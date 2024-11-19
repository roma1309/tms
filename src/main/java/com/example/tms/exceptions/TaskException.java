package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

public class TaskException extends ApiException{
    public TaskException(HttpStatus status, String title, String description) {
        super(status, title, description);
    }
}
