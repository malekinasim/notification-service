package com.nasim.notification_service.config.tenant;

public class TenantContext {
    private final static ThreadLocal<String> CURRENT_TENANT=new ThreadLocal<>();
    private TenantContext(){}
    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
