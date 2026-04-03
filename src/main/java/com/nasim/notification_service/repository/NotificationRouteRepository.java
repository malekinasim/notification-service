package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.NotificationRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRouteRepository extends JpaRepository<NotificationRoute,Long> {
}
