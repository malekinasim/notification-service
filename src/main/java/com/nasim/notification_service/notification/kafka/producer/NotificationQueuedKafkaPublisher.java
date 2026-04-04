package com.nasim.notification_service.notification.kafka.producer;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.notification.event.NotificationQueuedEvent;
import com.nasim.notification_service.notification.kafka.payload.NotificationQueeedMessage;
import com.nasim.notification_service.notification.service.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class NotificationQueuedKafkaPublisher {

    private final NotificationService notificationService;
    private final NotificationKafkaProducer notificationKafkaProducer;

    public NotificationQueuedKafkaPublisher(NotificationService notificationService, NotificationKafkaProducer notificationKafkaProducer) {
        this.notificationService = notificationService;
        this.notificationKafkaProducer = notificationKafkaProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleQueuedNotification(NotificationQueuedEvent event) {
        Notification notification = notificationService.findbyIdAndStatus(event.notificationId(), Notification.NotificationStatus.QUEUED);
        notificationKafkaProducer.sendQueuedMessageToProvider(new NotificationQueeedMessage(notification.getTenantID(),notification.getId(), LocalDateTime.now()));
    }
}
