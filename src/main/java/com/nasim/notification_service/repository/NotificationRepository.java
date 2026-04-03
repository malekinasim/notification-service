package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}