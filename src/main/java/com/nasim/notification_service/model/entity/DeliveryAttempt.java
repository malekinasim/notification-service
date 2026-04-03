package com.nasim.notification_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "delivery_attempt",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_route_attempt",
                        columnNames = {"notification_route_id", "attempt_number"}
                )
        }
)
public class DeliveryAttempt extends TenantBaseEntity<Long, String> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_route_id", nullable = false)
    private NotificationRoute notificationRoute;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Column(name = "request_payload_json", columnDefinition = "TEXT")
    private String requestPayloadJson;

    @Column(name = "response_payload_json", columnDefinition = "TEXT")
    private String responsePayloadJson;

    @Column(name = "success", nullable = false)
    private boolean success = false;

    @Column(name = "provider_message_id", length = 255)
    private String providerMessageId;

    @Column(name = "provider_status", length = 100)
    private String providerStatus;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}