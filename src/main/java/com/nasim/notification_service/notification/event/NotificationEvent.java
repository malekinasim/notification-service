package com.nasim.notification_service.notification.event;

import com.nasim.notification_service.model.entity.Notification;

public record NotificationEvent(
        Long notificationId,
        String tenantId,
        Notification.NotificationStatus status
) {
}
