package com.nasim.notification_service.notification.kafka.producer;

import com.nasim.notification_service.config.kafka.KafkaTopics;
import com.nasim.notification_service.notification.kafka.payload.NotificationQueeedMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationKafkaProducer {
    private final KafkaTemplate<String, NotificationQueeedMessage> kafkaTemplate;

    public NotificationKafkaProducer(KafkaTemplate<String, NotificationQueeedMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendQueuedMessageToProvider(NotificationQueeedMessage message) {
        String notificationQueuedTopic = KafkaTopics.NOTIFICATION_QUEUED.getKey();

        kafkaTemplate.send(
                notificationQueuedTopic,
                message.tenantID() + ":" + message.notificationId(),
                message
        );

    }
}
