package com.naas.category_service.dto.CtgCfgLoanPurpose;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CtgCfgLoanPurposeDto {
    private String purposeCode;
    private String purposeName;
    private String orgCode;
    private BigDecimal loanLimitMin;
    private BigDecimal loanLimitMax;
    private String riskLevel;
    private Boolean isActive;
    private String description;
}
