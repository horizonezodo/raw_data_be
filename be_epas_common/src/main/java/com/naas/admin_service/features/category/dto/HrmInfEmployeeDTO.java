package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HrmInfEmployeeDTO {
    private Long id;
    private String recordStatus = "approval";
    private int isActive;
    private String employeeCode;
    private String employeeName;
    private String userId;
    private String orgCode;
    private String currentAddress;
    private String permanentAddress;
    private String email;
    private Date dateOfBirth;
    private String genderCode;
    private String identificationId;
    private Date issueDate;
    private String issuePlace;
    private Date expiryDate;
    private String mobileNumber;
    private String positionCode;
    private String positionName;
    private String titleCode;
    private String titleName;
    private String maritalStatus;
    private String workingStatus;
    private String employeeTypeCode;
    private String customerCode;
}
