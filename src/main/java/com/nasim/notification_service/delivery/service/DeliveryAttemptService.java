package com.nasim.notification_service.delivery.service;

import com.nasim.notification_service.model.entity.DeliveryAttempt;
import com.nasim.notification_service.model.entity.NotificationRoute;


public interface DeliveryAttemptService {

    DeliveryAttempt create(Integer attemptNumber, NotificationRoute notificationRoute, String payloadJson);
    
    DeliveryAttempt updateStatus(DeliveryAttempt deliveryAttempt, boolean success, String errorCode, String errorMessage, boolean fetch);

    DeliveryAttempt findById(Long id);
}
