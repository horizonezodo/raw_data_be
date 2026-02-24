package com.naas.admin_service.features.mail.model;

import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_LOG_MAIL")
public class ComLogMail extends BaseEntity {

    @Column(name = "MAIL_TEMP_CODE", length = 64)
    private String mailTempCode;

    @Column(name = "PROCESS_INSTANCE_CODE", length = 128)
    private String processInstanceCode;

    @Column(name = "MAIL_STATUS", length = 16)
    private String mailStatus;
    
    @AuditIgnore
    @Column(name = "MAIL_SUBJECT", length = 128)
    private String mailSubject;

    @Column(name = "SEND_TIME")
    private Timestamp sendTime;

    @AuditIgnore
    @Column(name = "TO_EMAILS", length = 512)
    private String toEmails;

    @AuditIgnore
    @Column(name = "CC_EMAILS", length = 512)
    private String ccEmails;

    @AuditIgnore
    @Column(name = "BCC_EMAILS", length = 512)
    private String bccEmails;

    @AuditIgnore
    @Column(name = "MAIL_BODY", columnDefinition = "CLOB")
    private String mailBody;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
