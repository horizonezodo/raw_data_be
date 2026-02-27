package com.ngvgroup.bpm.core.liquibase.provision;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
public record TenantSchemaProvisionerRouter(List<TenantSchemaProvisioner> provisioners) {

    public TenantSchemaProvisionerRouter(List<TenantSchemaProvisioner> provisioners) {
        this.provisioners = (provisioners == null) ? List.of() : List.copyOf(provisioners);
    }

    public TenantSchemaProvisioner resolve(String dbType) {
        for (TenantSchemaProvisioner p : provisioners) {
            try {
                if (p != null && p.supports(dbType)) return p;
            } catch (Exception ignore) {
                // ignore
            }
        }

        String t = (dbType == null) ? "(null)" : dbType.trim().toUpperCase(Locale.ROOT);
        throw new IllegalArgumentException("No TenantSchemaProvisioner for dbType=" + t);
    }
}
