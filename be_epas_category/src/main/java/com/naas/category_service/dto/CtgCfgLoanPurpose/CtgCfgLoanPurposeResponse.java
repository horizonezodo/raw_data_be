package com.naas.category_service.dto.CtgCfgLoanPurpose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgLoanPurposeResponse {
    private Long id;
    private String purposeCode;
    private String purposeName;
    private String loanLimitMinFormat;
    private String loanLimitMaxFormat;
    private String riskLevel;
    private String status;
    private Boolean isActive;
    private BigDecimal loanLimitMin;
    private BigDecimal loanLimitMax;

    public CtgCfgLoanPurposeResponse(Long id, String purposeCode, String purposeName, BigDecimal loanLimitMin, BigDecimal loanLimitMax, String riskLevel, String status, Boolean isActive) {
        this.id = id;
        this.purposeCode = purposeCode;
        this.purposeName = purposeName;
        this.riskLevel = riskLevel;
        this.status = status;
        this.isActive = isActive;
        this.loanLimitMin = loanLimitMin;
        this.loanLimitMax = loanLimitMax;
    }
}
