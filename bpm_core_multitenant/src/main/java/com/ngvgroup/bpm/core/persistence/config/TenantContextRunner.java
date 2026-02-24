package com.ngvgroup.bpm.core.persistence.config;

import java.util.function.Supplier;

public final class TenantContextRunner {

    private TenantContextRunner() {}

    public static void runWithTenant(String tenantId, Runnable action) {
        String old = TenantContext.getTenantId();
        try {
            if (tenantId != null && !tenantId.isBlank()) {
                TenantContext.setTenantId(tenantId);
            }
            action.run();
        } finally {
            if (old != null && !old.isBlank()) TenantContext.setTenantId(old);
            else TenantContext.clear();
        }
    }

    public static <T> T callWithTenant(String tenantId, Supplier<T> supplier) {
        String old = TenantContext.getTenantId();
        try {
            if (tenantId != null && !tenantId.isBlank()) {
                TenantContext.setTenantId(tenantId);
            }
            return supplier.get();
        } finally {
            if (old != null && !old.isBlank()) TenantContext.setTenantId(old);
            else TenantContext.clear();
        }
    }
}
