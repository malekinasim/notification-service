package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.DeliveryAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAttemptRepository extends JpaRepository<DeliveryAttempt,Long> {
}
