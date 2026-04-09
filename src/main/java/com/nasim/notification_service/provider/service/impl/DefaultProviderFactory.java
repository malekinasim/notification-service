package com.nasim.notification_service.provider.service.impl;

import com.nasim.notification_service.model.entity.Provider;
import com.nasim.notification_service.provider.service.NotificationProviderClient;
import com.nasim.notification_service.provider.service.ProviderFactory;
import com.nasim.notification_service.shared.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultProviderFactory implements ProviderFactory {

    private final List<NotificationProviderClient> clients;

    public DefaultProviderFactory(List<NotificationProviderClient> clients) {
        this.clients = clients;
    }

    @Override
    public NotificationProviderClient getClient(Provider.ProviderType providerType) {
        return clients.stream()
                .filter(c -> c.supports(providerType))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException("No_provider_client_found", providerType)
                );
    }
}
