package com.nasim.notification_service.exception;

public class TenantResolutionException extends RuntimeException{
    public TenantResolutionException(String message) {
        super(message);
    }
}
