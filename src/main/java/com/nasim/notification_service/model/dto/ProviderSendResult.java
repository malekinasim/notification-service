package com.nasim.notification_service.model.dto;

public record ProviderSendResult(
        boolean success,
        String providerMessageId,
        String providerStatus,
        String errorCode,
        String errorMessage
){
}
