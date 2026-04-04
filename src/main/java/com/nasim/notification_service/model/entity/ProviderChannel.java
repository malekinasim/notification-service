package com.nasim.notification_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "provider_channel",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_provider_channel", columnNames = {"channel_id", "provider_id"})
        }
)
@Getter
@Setter
public class ProviderChannel extends TenantBaseEntity<Long, String> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    private NotificationChannel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "is_default", nullable = false)
    private boolean defaultProvider = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private PriorityLevel priority = PriorityLevel.NORMAL;

    public enum PriorityLevel {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }
}