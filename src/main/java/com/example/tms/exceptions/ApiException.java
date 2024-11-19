package com.example.tms.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String title;
    private final String description;
    private final LocalDateTime time;

    public ApiException(HttpStatus status, String title, String description) {
        this.status = status;
        this.title = title;
        this.description = description;
        this.time = LocalDateTime.now();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
        return time;
    }
}