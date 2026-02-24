package com.naas.admin_service.features.category.model;

import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_INF_USER")
public class HrmInfEmployee extends BaseEntity {
    @Column(name = "EMPLOYEE_CODE", length = 128, nullable = false)
    private String employeeCode;

    @Column(name = "EMPLOYEE_NAME", length = 256, nullable = false)
    private String employeeName;

    @Column(name = "USER_ID", length = 64, nullable = false)
    private String userId;

    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @AuditIgnore
    @Column(name = "CURRENT_ADDRESS", length = 512, nullable = false)
    private String currentAddress;

    @AuditIgnore
    @Column(name = "PERMANENT_ADDRESS", length = 512, nullable = false)
    private String permanentAddress;

    @AuditIgnore
    @Column(name = "EMAIL", length = 256)
    private String email;

    @AuditIgnore
    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private Date dateOfBirth;

    @Column(name = "GENDER_CODE", length = 16)
    private String genderCode;

    @AuditIgnore
    @Column(name = "IDENTIFICATION_ID", length = 128, nullable = false)
    private String identificationId;

    @AuditIgnore
    @Column(name = "ISSUE_DATE", nullable = false)
    private Date issueDate;

    @AuditIgnore
    @Column(name = "ISSUE_PLACE", length = 256, nullable = false)
    private String issuePlace;

    @AuditIgnore
    @Column(name = "EXPIRY_DATE", nullable = false)
    private Date expiryDate;

    @AuditIgnore
    @Column(name = "MOBILE_NUMBER", length = 128, nullable = false)
    private String mobileNumber;

    @Column(name = "POSITION_CODE", length = 32, nullable = false)
    private String positionCode;

    @Column(name = "POSITION_NAME", length = 128, nullable = false)
    private String positionName;

    @Column(name = "TITLE_CODE", length = 32, nullable = false)
    private String titleCode;

    @Column(name = "TITLE_NAME", length = 128, nullable = false)
    private String titleName;

    @Column(name = "MARITAL_STATUS", length = 64)
    private String maritalStatus;

    @Column(name = "WORKING_STATUS", length = 64)
    private String workingStatus;

    @Column(name = "EMPLOYEE_TYPE_CODE", length = 64)
    private String employeeTypeCode;

    @Column(name = "CUSTOMER_CODE", length = 128)
    private String customerCode;

            @Column(
      name = "RECORD_STATUS",
      length = 64
   )
   private String recordStatus = "approval";

   @Column(
      name = "IS_ACTIVE",
      nullable = false
   )
   private Integer isActive = 1;
}
