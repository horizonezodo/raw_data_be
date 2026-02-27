package com.ngvgroup.bpm.core.liquibase.provision;

/**
 * Strategy interface for provisioning (creating) a tenant schema per service.
 *
 * <p>Contract:</p>
 * <ul>
 *   <li>{@link #supports(String)} decides which DB type the implementation handles.</li>
 *   <li>{@link #ensureSchemaExists(String, String, String)} provisions the schema if missing.</li>
 * </ul>
 *
 * <p>Notes:</p>
 * <ul>
 *   <li>Oracle: typically creates a USER (schema owner) + grants, using schemaPassword.</li>
 *   <li>Postgres: in your setup, no ROLE creation; use the tenant row USER to create schemas.
 *       schemaPassword may be ignored.</li>
 * </ul>
 */
public interface TenantSchemaProvisioner {

    boolean supports(String dbType);

    void ensureSchemaExists(String tenantId, String schema, String schemaPassword) throws Exception;
}
