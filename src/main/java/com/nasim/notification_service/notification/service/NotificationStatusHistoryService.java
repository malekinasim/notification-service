package com.nasim.notification_service.notification.service;

import com.nasim.notification_service.model.entity.Notification;

public interface NotificationStatusHistoryService {
    void record(Notification notification, Notification.NotificationStatus status, String reason);
}