package com.nasim.notification_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class TenantBaseEntity<ID, TenantID> extends BaseEntity<ID> {
    @Column(name = "tenant_id", nullable = false)
    private TenantID tenantID;
}
