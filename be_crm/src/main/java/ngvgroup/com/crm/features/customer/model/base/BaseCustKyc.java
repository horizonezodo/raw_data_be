package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustKyc extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "KYC_TYPE_CODE", length = 64)
    private String kycTypeCode;

    @Column(name = "KYC_REASON_CODE", length = 64)
    private String kycReasonCode;

    @Column(name = "KYC_RESULT_CODE", length = 64)
    private String kycResultCode;

    @Column(name = "KYC_RISK_SCORE", precision = 7, scale = 4)
    private BigDecimal kycRiskScore;

    @Column(name = "KYC_RISK_LEVEL", length = 64)
    private String kycRiskLevel;

    @Column(name = "SANCTION_RESULT", length = 64)
    private String sanctionResult;

    @Column(name = "IS_PEP")
    private Integer isPep;

    @Column(name = "KYC_DATE")
    private LocalDateTime kycDate;

    @Column(name = "NEXT_REVIEW_DATE")
    private Date nextReviewDate;

    @Column(name = "INTERNAL_NOTE", length = 512)
    private String internalNote;
}