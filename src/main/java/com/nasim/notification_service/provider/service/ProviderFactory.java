package com.nasim.notification_service.provider.service;

import com.nasim.notification_service.model.entity.Provider;

public interface ProviderFactory {
    NotificationProviderClient getClient(Provider.ProviderType providerType);
}
