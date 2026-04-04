package com.nasim.notification_service.notification.service.impl;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.NotificationStatusHistory;
import com.nasim.notification_service.repository.NotificationStatusHistoryRepository;
import com.nasim.notification_service.notification.service.NotificationStatusHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationStatusHistoryServiceImpl implements NotificationStatusHistoryService {

    private final NotificationStatusHistoryRepository notificationStatusHistoryRepository;

    public NotificationStatusHistoryServiceImpl(
            NotificationStatusHistoryRepository notificationStatusHistoryRepository
    ) {
        this.notificationStatusHistoryRepository = notificationStatusHistoryRepository;
    }

    @Override
    public void record(Notification notification, Notification.NotificationStatus status, String reason) {
        NotificationStatusHistory history = new NotificationStatusHistory();
        history.setNotification(notification);
        history.setStatus(status);
        history.setReason(reason);
        history.setChangedAt(LocalDateTime.now());
        history.setTenantID(notification.getTenantID());

        notificationStatusHistoryRepository.save(history);
    }
}