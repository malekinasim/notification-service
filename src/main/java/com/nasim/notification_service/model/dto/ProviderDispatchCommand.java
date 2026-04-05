package com.nasim.notification_service.model.dto;

import com.nasim.notification_service.model.entity.NotificationChannel;
import com.nasim.notification_service.model.entity.Provider;
import com.nasim.notification_service.model.entity.Template;

public record ProviderDispatchCommand(
        Long notificationId,
        Long notificationRouteId,
        String tenantId,

        NotificationChannel.ChannelType channelType,
        Provider.ProviderType providerType,

        String recipientAddress,
        String recipientName,
        String senderAddress,
        String senderName,

        String body,
        Template.TemplateContentType contentType
) {
}
