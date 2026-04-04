
package com.nasim.notification_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification", indexes = {
        @Index(name = "idx_notification_template_policy", columnList = "template_id,routing_policy_id")
})
public class Notification extends TenantBaseEntity<Long, String> {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "routing_policy_id", nullable = false)
    private RoutingPolicy routingPolicy;

    @Column(name = "recipient_address", nullable = false, length = 255)
    private String recipientAddress;

    @Column(name = "recipient_name", length = 150)
    private String recipientName;

    @Column(name = "sender_address", length = 255)
    private String senderAddress;

    @Column(name = "sender_name", length = 150)
    private String senderName;


    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;


    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false, length = 30)
    private NotificationStatus currentStatus = NotificationStatus.CREATED;

    public enum NotificationStatus {
        CREATED,
        QUEUED,
        PROCESSING,
        SENT,
        FAILED,
        RETRYING,
        CANCELLED
    }
}
