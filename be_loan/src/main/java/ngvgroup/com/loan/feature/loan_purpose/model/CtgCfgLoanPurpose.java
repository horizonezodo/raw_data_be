package ngvgroup.com.loan.feature.loan_purpose.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_LOAN_PURPOSE")
public class CtgCfgLoanPurpose extends BaseEntity {

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

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
