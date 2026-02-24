package com.naas.admin_service.features.category.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "COM_INF_CREDIT_INST")
@AllArgsConstructor
@NoArgsConstructor
public class CtgInfCreditInst extends BaseEntity {
    @Column(name = "CREDIT_INST_CODE", nullable = false, length = 32)
    private String creditInstCode;
    @Column(name = "CREDIT_INST_NAME", nullable = false, length = 256)
    private String creditInstName;
    @Column(name = "CREDIT_INST_SHORT_NAME", length = 128)
    private String creditInstShortName;

    @AuditIgnore
    @Column(name = "ADDRESS", length = 256)
    private String address;

    @AuditIgnore
    @Column(name = "PHONE_NUMBER", length = 128)
    private String phoneNumber;

    @AuditIgnore
    @Column(name = "EMAIL", length = 128)
    private String email;
    @Column(name = "WEBSITE", length = 256)
    private String website;
    @Column(name = "TAX_CODE", length = 64)
    private String taxCode;
    @Column(name = "LICENSE_NO", length = 128)
    private String licenseNo;
    @Column(name = "LICENSE_DATE")
    private Date licenseDate;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
