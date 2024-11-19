package com.example.tms.dto;

import com.example.tms.entity.enums.Priority;
import com.example.tms.entity.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class TaskDto {
    private Long id;
    private String name;
    private String description;

    private Long userId;

    private String status;

    private String priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
