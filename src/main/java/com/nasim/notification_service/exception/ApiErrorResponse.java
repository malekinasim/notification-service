package com.nasim.notification_service.exception;



import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {
}