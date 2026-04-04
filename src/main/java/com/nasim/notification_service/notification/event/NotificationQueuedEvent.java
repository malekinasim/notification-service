package com.nasim.notification_service.notification.event;

public record NotificationQueuedEvent(
        Long notificationId,
        String tenantId
) {
}
