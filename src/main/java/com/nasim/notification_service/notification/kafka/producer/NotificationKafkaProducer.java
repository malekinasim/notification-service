package com.nasim.notification_service.notification.kafka.producer;

import com.nasim.notification_service.config.kafka.KafkaTopics;
import com.nasim.notification_service.notification.kafka.payload.NotificationQueuedMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationKafkaProducer {
    private final KafkaTemplate<String, NotificationQueuedMessage> kafkaTemplate;

    public NotificationKafkaProducer(KafkaTemplate<String, NotificationQueuedMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendQueuedMessageToProvider(NotificationQueuedMessage message) {
        String notificationQueuedTopic = KafkaTopics.NOTIFICATION_QUEUED.getKey();

        kafkaTemplate.send(
                notificationQueuedTopic,
                message.tenantID() + ":" + message.notificationId(),
                message
        );

    }
}
