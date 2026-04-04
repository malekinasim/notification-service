package com.nasim.notification_service.shared.exception;


import java.time.LocalDateTime;
public record ApiErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {
}