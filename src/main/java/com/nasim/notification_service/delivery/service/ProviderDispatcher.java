package com.nasim.notification_service.delivery.service;

import com.nasim.notification_service.model.dto.ProviderResponse;
import com.nasim.notification_service.model.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ProviderDispatcher {
    public ProviderResponse dispatchMessageToProvider(Notification notification, NotificationRoute notificationRoute, RoutingPolicy routingPolicy, RoutingPolicyStep routingPolicyStep, DeliveryAttempt deliveryAttempt) {

       return new ProviderResponse(true,1L,
               "success",201,"success");
    }
}
