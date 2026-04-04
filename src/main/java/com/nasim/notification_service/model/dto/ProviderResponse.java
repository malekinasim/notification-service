package com.nasim.notification_service.model.dto;

public record ProviderResponse(
        Boolean success,
        Long providerMessageId,
        String providerStatus,
        int errorCode,
        String errorMessage
){
}
