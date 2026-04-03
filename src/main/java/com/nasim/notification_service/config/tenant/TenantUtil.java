package com.nasim.notification_service.config.tenant;

import com.nasim.notification_service.exception.TenantResolutionException;

public class TenantUtil {
    public static String getTenantId(){

        String tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new TenantResolutionException("Tenant not found in context");
        }
        return tenantId;
    }
}
