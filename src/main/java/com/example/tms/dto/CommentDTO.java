package com.example.tms.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentDTO {
    private Long id;
    @NotBlank(message = "message is required")
    private String message;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
