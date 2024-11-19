package com.example.tms.entity.enums;

import com.example.tms.exceptions.StatusNotFoundException;
import org.springframework.http.HttpStatus;

public enum Priority {
    HIGH, AVERAGE, LOW;

    public static Priority getPriority(String priorityStr) {
        for (Priority priority : Priority.values()) {
            if (priority.name().equals(priorityStr)) {
                return priority;
            }
        }
        throw new StatusNotFoundException(HttpStatus.BAD_REQUEST, "wrong priority", "priority not found");
    }

    public static Priority getPriorityWithNull(String priorityStr) {
        for (Priority priority : Priority.values()) {
            if (priority.name().equals(priorityStr)) {
                return priority;
            }
        }
        return null;
    }
}
