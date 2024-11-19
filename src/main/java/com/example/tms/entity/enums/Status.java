package com.example.tms.entity.enums;

import com.example.tms.exceptions.StatusNotFoundException;
import org.springframework.http.HttpStatus;

public enum Status {
    IN_PROGRESS, COMPLETED, IN_WAITING;


    public static Status getStatus(String statusStr) {
        for (Status status : Status.values()) {
            if (status.name().equals(statusStr)) {
                return status;
            }
        }
        throw new StatusNotFoundException(HttpStatus.BAD_REQUEST, "wrong status", "status not found");
    }

    public static Status getStatusWithNull(String statusStr) {
        for (Status status : Status.values()) {
            if (status.name().equals(statusStr)) {
                return status;
            }
        }
        return null;
    }
}
