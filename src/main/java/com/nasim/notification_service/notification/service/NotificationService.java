package com.nasim.notification_service.notification.service;

import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.model.entity.Notification;
import jakarta.validation.Valid;

import java.util.List;

public interface NotificationService {
   Notification findById(Long notificationId);

    Notification create(@Valid NotificationDto request);

    List<Notification> listNotificationByStaus(Notification.NotificationStatus status);

    Notification findbyIdAndStatus(Long notificationId, Notification.NotificationStatus notificationStatus);

    Notification updateNotificationStatus(Notification notification, Notification.NotificationStatus status, String reason, Boolean fetch);
}
