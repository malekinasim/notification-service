package com.nasim.notification_service.config.kafka;

import lombok.Getter;

@Getter
public enum KafkaTopics {
    NOTIFICATION_QUEUED("notification.queued");
    private final String key;
    private KafkaTopics(String key){
        this.key=key;
    }
}
