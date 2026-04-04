package com.nasim.notification_service.delivery.service;

import com.nasim.notification_service.notification.kafka.payload.NotificationQueuedMessage;

public interface NotificationDeliveryService {

    public void sendQueuedMessageToProvider(NotificationQueuedMessage message);

}
