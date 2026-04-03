package com.nasim.notification_service.controller;

import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.model.dto.NotificationMapper;
import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @PostMapping("/notification")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NotificationDto> createNotification(
            @Valid @RequestBody NotificationDto request
    ) {
        Notification response= notificationService.create(request);
        if(response!=null)
            return new ResponseEntity<>(notificationMapper.map(response),HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
