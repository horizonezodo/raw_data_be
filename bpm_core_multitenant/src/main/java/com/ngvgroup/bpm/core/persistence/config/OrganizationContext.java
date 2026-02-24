package com.ngvgroup.bpm.core.persistence.config;

public final class OrganizationContext {
    private static final ThreadLocal<String> CURRENT_ORG_ALIAS = new ThreadLocal<>();

    private OrganizationContext() {}

    public static void setOrgAlias(String orgAlias) {
        CURRENT_ORG_ALIAS.set(orgAlias);
    }

    public static String getOrgAlias() {
        return CURRENT_ORG_ALIAS.get();
    }

    public static void clear() {
        CURRENT_ORG_ALIAS.remove();
    }
}
