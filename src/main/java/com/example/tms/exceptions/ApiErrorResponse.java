package com.example.tms.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiErrorResponse {
    private HttpStatus status;
    private String title;
    private String description;
    @JsonFormat
            (pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime time;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(HttpStatus status, String title, String description) {
        this.status = status;
        this.title = title;
        this.description = description;
        this.time = LocalDateTime.now();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
