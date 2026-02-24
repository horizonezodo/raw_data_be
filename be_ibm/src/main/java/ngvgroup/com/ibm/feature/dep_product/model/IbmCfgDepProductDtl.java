package ngvgroup.com.ibm.feature.dep_product.model;

import java.sql.Date;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "IBM_CFG_DEP_PRODUCT_DTL")
@Getter
@Setter
@ToString
public class IbmCfgDepProductDtl extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "IBM_DEP_PRODUCT_CODE", nullable = false, length = 128)
    private String ibmDepProductCode;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Date effectiveDate;

    @Column(name = "INTEREST_CALC_METHOD", nullable = false, length = 128)
    private String interestCalcMethod;

    @Column(name = "INTEREST_BASE_CODE", nullable = false, length = 128)
    private String interestBaseCode;

    @Column(name = "INTEREST_DATE_METHOD", nullable = false, length = 128)
    private String interestDateMethod;

    @Column(name = "IS_PARTIAL_WITHDRAW", nullable = false)
    private Integer isPartialWithdraw;

    @Column(name = "IS_EARLY_SETTLEMENT", nullable = false)
    private Integer isEarlySettlement;

    @Column(name = "INTEREST_START_METHOD", nullable = false, length = 128)
    private String interestStartMethod;

    @Column(name = "MATURITY_DATE_RULE", nullable = false, length = 128)
    private String maturityDateRule;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
}
