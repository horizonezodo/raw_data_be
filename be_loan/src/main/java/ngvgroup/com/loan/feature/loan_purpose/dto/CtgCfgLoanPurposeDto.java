package ngvgroup.com.loan.feature.loan_purpose.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CtgCfgLoanPurposeDto {
    private String purposeCode;
    private String purposeName;
    private String orgCode;
    private BigDecimal loanLimitMin;
    private BigDecimal loanLimitMax;
    private String riskLevel;
    private Integer isActive;
    private String description;
}
