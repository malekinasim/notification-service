package com.nasim.notification_service.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "routing_policy", uniqueConstraints = {
        @UniqueConstraint(name = "uk_routing_policy_code", columnNames = "code")
},indexes = {@Index(name = "idx_routing_policy_tenant_templat",columnList = "template_id,tenant_id")})
public class RoutingPolicy extends BaseEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id" , nullable = false)
    private Template template;
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;
    @Column(name = "name", nullable = false, length = 150)
    private String name;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "is_default", nullable = false)
    private boolean defaultPolicy;


}
