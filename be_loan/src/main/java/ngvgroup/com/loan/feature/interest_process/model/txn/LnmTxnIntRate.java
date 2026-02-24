package ngvgroup.com.loan.feature.interest_process.model.txn;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "LNM_TXN_INT_RATE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LnmTxnIntRate extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "CURRENCY_CODE",  nullable = false, length = 4)
    private String currencyCode;

    @Column(name = "PROCESS_TYPE_CODE",  nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_INSTANCE_CODE",  nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "INTEREST_RATE_CODE",  nullable = false, length = 64)
    private String interestRateCode;

    @Column(name = "INTEREST_RATE_NAME",  nullable = false, length = 256)
    private String interestRateName;

    @Column(name = "INTEREST_RATE_TYPE",  nullable = false, length = 64)
    private String interestRateType;

    @Column(name = "APPLY_TYPE",  nullable = false, length = 64)
    private String applyType;

    @Column(name = "BUSINESS_STATUS", length = 32, nullable = false)
    private String businessStatus;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
