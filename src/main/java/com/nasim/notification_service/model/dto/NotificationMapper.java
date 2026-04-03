package com.nasim.notification_service.model.dto;

import com.nasim.notification_service.model.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDto map(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getTemplate().getCode(),
                notification.getRoutingPolicy().getCode(),
                notification.getRecipientAddress(),
                notification.getCurrentStatus().name(),
                notification.getCreatedAt()
        );
    }
}