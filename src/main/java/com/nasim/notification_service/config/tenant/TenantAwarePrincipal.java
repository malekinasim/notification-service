package com.nasim.notification_service.config.tenant;

import org.springframework.security.core.userdetails.UserDetails;

public interface TenantAwarePrincipal extends UserDetails {
    public String getTenantId();
}
