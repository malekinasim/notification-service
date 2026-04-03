package com.nasim.notification_service.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "notification_route",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_route_order",
                        columnNames = {"notification_id", "route_order"}
                )
        }
)
public class NotificationRoute extends TenantBaseEntity<Long, String> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routing_policy_step_id", nullable = false)
    private RoutingPolicyStep routingPolicyStep;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RouteStatus status = RouteStatus.PENDING;

    public enum RouteStatus {
        PENDING,
        PROCESSING,
        SENT,
        FAILED,
        SKIPPED
    }

}
