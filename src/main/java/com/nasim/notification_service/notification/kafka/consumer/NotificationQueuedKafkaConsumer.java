package com.nasim.notification_service.notification.kafka.consumer;

import com.nasim.notification_service.config.tenant.TenantContext;
import com.nasim.notification_service.delivery.service.NotificationDeliveryService;
import com.nasim.notification_service.notification.kafka.payload.NotificationQueuedMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationQueuedKafkaConsumer {
    private final NotificationDeliveryService notificationDeliveryService;

    public NotificationQueuedKafkaConsumer(NotificationDeliveryService notificationDeliveryService) {
        this.notificationDeliveryService = notificationDeliveryService;
    }


    @KafkaListener(
            topics = "#{T(com.nasim.notification_service.config.kafka).NOTIFICATION_QUEUED.getKey()}"
    )
    public void listenToNotificationQueuedMessage(NotificationQueuedMessage message) {
        try {
            TenantContext.setTenantId(message.tenantID());
            notificationDeliveryService.sendQueuedMessageToProvider(message);
        } finally {
            TenantContext.clear();
        }

    }
}
