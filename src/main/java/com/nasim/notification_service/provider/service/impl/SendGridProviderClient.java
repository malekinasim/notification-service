package com.nasim.notification_service.provider.service.impl;

import com.nasim.notification_service.model.dto.ProviderDispatchCommand;
import com.nasim.notification_service.model.dto.ProviderSendResult;
import com.nasim.notification_service.model.entity.Provider;
import com.nasim.notification_service.provider.service.NotificationProviderClient;
import org.springframework.stereotype.Component;

@Component
public class SendGridProviderClient implements NotificationProviderClient {

    @Override
    public boolean supports(Provider.ProviderType providerType) {
        return providerType == Provider.ProviderType.SENDGRID;
    }

    @Override
    public ProviderSendResult send(ProviderDispatchCommand command) {
        // fake implementation for now

        return new ProviderSendResult(
                true,
                "sendgrid-msg-id-123",
                "SENT",
                null,
                null
        );
    }
}