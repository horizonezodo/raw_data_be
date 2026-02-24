package ngvgroup.com.loan.feature.loan_purpose.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer isActive;
    private BigDecimal loanLimitMin;
    private BigDecimal loanLimitMax;

    public CtgCfgLoanPurposeResponse(Long id, String purposeCode, String purposeName, BigDecimal loanLimitMin, BigDecimal loanLimitMax,
                                     String riskLevel, Integer isActive) {
        this.id = id;
        this.purposeCode = purposeCode;
        this.purposeName = purposeName;
        this.riskLevel = riskLevel;
        this.isActive = isActive;
        this.loanLimitMin = loanLimitMin;
        this.loanLimitMax = loanLimitMax;
    }
}
