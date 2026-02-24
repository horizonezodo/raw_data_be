package com.naas.admin_service.features.mail.model;

import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_CFG_MAIL_FROM")
public class ComCfgMailFrom extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "MAIL_CODE", length = 128)
    private String mailCode;

    @AuditIgnore
    @Column(name = "MAIL_FROM", length = 64)
    private String mailFrom;

    @Column(name = "MAIL_FROM_NAME", length = 128)
    private String mailFromName;

    @AuditIgnore
    @Column(name = "MAIL_PASSWORD", length = 128)
    private String mailPassword;

    @Column(name = "IS_ENCRYPTED")
    private Boolean isEncrypted;

    @AuditIgnore
    @Column(name = "HOST", length = 256)
    private String host;

    @AuditIgnore
    @Column(name = "PORT")
    private Integer port;

    @AuditIgnore
    @Column(name = "MAIL_PROTOCOL", length = 64)
    private String mailProtocol;

    @AuditIgnore
    @Column(name = "IS_USE_SSL")
    private Boolean isUseSsl;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
