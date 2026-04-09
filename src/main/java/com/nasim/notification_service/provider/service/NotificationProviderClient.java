package com.nasim.notification_service.provider.service;

import com.nasim.notification_service.model.dto.ProviderDispatchCommand;
import com.nasim.notification_service.model.dto.ProviderSendResult;
import com.nasim.notification_service.model.entity.Provider;

public interface NotificationProviderClient {
    boolean supports(Provider.ProviderType providerType);
    ProviderSendResult send(ProviderDispatchCommand command);
}