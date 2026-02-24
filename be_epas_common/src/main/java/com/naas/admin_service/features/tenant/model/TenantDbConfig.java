package com.naas.admin_service.features.tenant.model;

import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TENANT_DB_CONFIG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDbConfig extends BaseEntity {

    @Column(name = "TENANT_ID", length = 100, nullable = false)
    private String tenantId;

    @Column(name = "DB_TYPE", length = 20, nullable = false)
    private String dbType;

    @Column(name = "JDBC_URL", length = 500, nullable = false)
    private String jdbcUrl;

    @AuditIgnore
    @Column(name = "USERNAME", length = 200, nullable = false)
    private String username;

    @Column(name = "PASSWORD", length = 500, nullable = false)
    private String password;

    @Column(name = "DRIVER_CLASS", length = 200, nullable = false)
    private String driverClass;

    @Column(name = "ISSUER_URI", length = 500)
    private String issuerUri;

    @Column(name = "ACTIVE", length = 1, nullable = false)
    private String active = "Y";

}