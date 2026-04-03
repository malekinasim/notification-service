package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.NotificationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  NotificationStatusHistoryRepository extends JpaRepository<NotificationStatusHistory, Long> {
}
