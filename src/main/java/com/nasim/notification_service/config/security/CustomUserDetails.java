package com.nasim.notification_service.config.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    public String getTenantId();
}
