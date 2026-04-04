package com.nasim.notification_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notification_channel", indexes = {
        @Index(name = "idx_channel_type", columnList = "type")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_channel_type", columnNames = "type")
})
public class NotificationChannel extends BaseEntity<Long> {
    @Column(name = "type", nullable = false, length = 50, unique = true)
    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(name = "display_name", nullable = false, length = 200)
    private String displayName;

    public enum ChannelType {
        EMAIL, SMS, PUSH, TELEGRAM, WHATSAPP;
    }
}
