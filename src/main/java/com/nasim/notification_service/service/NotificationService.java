package com.nasim.notification_service.service;

import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.model.entity.Notification;
import jakarta.validation.Valid;

public interface NotificationService  {


    Notification create(@Valid NotificationDto request);
}
