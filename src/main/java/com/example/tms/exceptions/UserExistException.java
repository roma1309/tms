package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

public class UserExistException extends ApiException{
    public UserExistException(HttpStatus status, String title, String description) {
        super(status, title, description);
    }
}