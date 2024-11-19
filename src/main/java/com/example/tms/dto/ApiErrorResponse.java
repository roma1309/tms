package com.example.tms.dto;

import java.time.LocalDateTime;

public class ApiErrorResponse {
    private final String title;
    private final String description;
    private final LocalDateTime time;

    public ApiErrorResponse(String title, String description, LocalDateTime time) {
        this.title = title;
        this.description = description;
        this.time = time;
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
