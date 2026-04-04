package com.nasim.notification_service.notification.kafka.payload;

import java.time.LocalDateTime;
public record NotificationQueuedMessage(
        String tenantID,
        Long notificationId,
        LocalDateTime timestamp
) {
}
