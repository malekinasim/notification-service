package com.nasim.notification_service.notification.kafka.producer;

import com.nasim.notification_service.model.entity.Notification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public NotificationKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendQueuedMessageToProvider(Notification notification) {
        String topicName = "notification.Queued";
        Map<String, Object> message = new HashMap<>();
        message.put("notification_id", notification.getId());
        message.put("tenant_id", notification.getTenantID());
        kafkaTemplate.send(topicName, message);
    }
}
