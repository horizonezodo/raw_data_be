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
@Table(name = "COM_CFG_MAIL_TEMPLATE")
public class ComCfgMailTemplate extends BaseEntity {
    @Column(name = "MAIL_TEMPALTE_CODE", nullable = false, length = 64)
    private String mailTemplateCode;

    @Column(name = "MAIL_TEMPALTE_NAME", length = 256)
    private String mailTemplateName;

    @Column(name = "MAIL_CODE", length = 128)
    private String mailCode;

    @Column(name = "MAIL_SUBJECT", length = 128)
    private String mailSubject;

    @Column(name = "MAIL_BODY", columnDefinition = "CLOB")
    private String mailBody;

    @Column(name = "SENDER_NAME", length = 256)
    private String senderName;

    @AuditIgnore
    @Column(name = "SENDER_EMAIL", length = 256)
    private String senderEmail;

    @AuditIgnore
    @Column(name = "TO_EMAILS", length = 512)
    private String toEmails;

    @AuditIgnore
    @Column(name = "CC_EMAILS", length = 512)
    private String ccEmails;

    @AuditIgnore
    @Column(name = "BCC_EMAILS", length = 512)
    private String bccEmails;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
