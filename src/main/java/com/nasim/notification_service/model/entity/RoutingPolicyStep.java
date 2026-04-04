package com.nasim.notification_service.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
@Table(name = "routing_policy_step", uniqueConstraints = {
        @UniqueConstraint(name = "uk_routing_policy_step_policy_provider",
                columnNames = {"routing_policy_id", "provider_channel_id"}),
        @UniqueConstraint(
                name = "uk_routing_policy_step_order",
                columnNames = {"routing_policy_id", "step_order"}
        )
}, indexes = {@Index(name = "idx_policy_step_order", columnList = "routing_policy_id, step_order")})
public class RoutingPolicyStep extends BaseEntity<Long> {
    @ManyToOne(optional = false)
    @JoinColumn(name = "routing_policy_id", nullable = false)
    private RoutingPolicy routingPolicy;
    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_channel_id", nullable = false)
    private ProviderChannel providerChannel;
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;
    @Column(name = "max_retry", nullable = false)
    private Integer maxRetry = 1;

}
