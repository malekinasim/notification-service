package com.nasim.notification_service.notification.kafka.producer;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.notification.event.NotificationEvent;
import com.nasim.notification_service.notification.service.NotificationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationQueuedKafkaPublisher {

    private final NotificationService notificationService;
    private final NotificationKafkaProducer notificationKafkaProducer;

    public NotificationQueuedKafkaPublisher(NotificationService notificationService, NotificationKafkaProducer notificationKafkaProducer) {
        this.notificationService = notificationService;
        this.notificationKafkaProducer = notificationKafkaProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleQueuedNotification(NotificationEvent event) {
        Notification notification = notificationService.findbyIdAndStatus(event.notificationId(), event.status());
        notificationKafkaProducer.sendQueuedMessageToProvider(notification);
    }



}
