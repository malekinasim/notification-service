package com.nasim.notification_service.exception;

import com.nasim.notification_service.config.resourceBundel.MessageUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;

    public GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleApiResponseError(BusinessException exception){
       return buildResponse(HttpStatus.BAD_REQUEST,messageUtil.get(exception.getMessageKey(),
               exception.getArgs()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.CONFLICT,
                "Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage());

    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(TenantResolutionException.class)
    public ResponseEntity<ApiErrorResponse> handleTenantNotFound(TenantResolutionException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());

    }
    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message) {
        ApiErrorResponse err = new ApiErrorResponse(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(err, status);
    }
}
