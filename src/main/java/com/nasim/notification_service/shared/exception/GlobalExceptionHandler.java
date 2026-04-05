package com.nasim.notification_service.shared.exception;

import com.nasim.notification_service.shared.resourceBundel.MessageUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;

    public GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleApiResponseError(BusinessException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, messageUtil.get(exception.getMessageKey(),
                exception.getArgs()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityException(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.CONFLICT,
                "Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage());

    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return buildResponse(HttpStatus.BAD_REQUEST,
               "fields' error "+errors.toString() );

    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(TenantResolutionException.class)
    public ResponseEntity<ApiErrorResponse> handleTenantNotFound(TenantResolutionException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal server error");
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message) {
        ApiErrorResponse err = new ApiErrorResponse(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(err, status);
    }
}
