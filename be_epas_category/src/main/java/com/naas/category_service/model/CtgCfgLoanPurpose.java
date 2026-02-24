package com.naas.category_service.model;

import com.naas.category_service.constant.StatusConstants;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_LOAN_PURPOSE")
public class CtgCfgLoanPurpose extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "PURPOSE_CODE", nullable = false, length = 32)
    private String purposeCode;

    @Column(name = "PURPOSE_NAME", length = 256)
    private String purposeName;

    @Column(name = "RISK_LEVEL", length = 128)
    private String riskLevel;

    @Column(name = "LOAN_LIMIT_MIN", precision = 22, scale = 4)
    private BigDecimal loanLimitMin;

    @Column(name = "LOAN_LIMIT_MAX", precision = 22, scale = 4)
    private BigDecimal loanLimitMax;

    public CtgCfgLoanPurpose(Boolean isActive, String orgCode, String purposeCode, String purposeName, String riskLevel, BigDecimal loanLimitMin, BigDecimal loanLimitMax,String description) {
        this.isActive = isActive;
        this.orgCode = orgCode;
        this.purposeCode = purposeCode;
        this.purposeName = purposeName;
        this.riskLevel = riskLevel;
        this.loanLimitMin = loanLimitMin;
        this.loanLimitMax = loanLimitMax;
        this.setDescription(description);
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
    }
}
