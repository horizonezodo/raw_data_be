package com.naas.category_service.dto.CtgInfCreditInst;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfCreditInstDTO {
    private Long id;
    private String creditInstCode;
    private String creditInstName;
    private String creditInstShortName;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String taxCode;
    private String licenseNo;
    private Date licenseDate;
    private int isDelete;
    private String description;
    private String status;
}
