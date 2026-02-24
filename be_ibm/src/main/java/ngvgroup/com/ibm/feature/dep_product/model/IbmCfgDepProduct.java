package ngvgroup.com.ibm.feature.dep_product.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "IBM_CFG_DEP_PRODUCT")
@Getter
@Setter
public class IbmCfgDepProduct extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 4)
    private String currencyCode;

    @Column(name = "IBM_DEP_PRODUCT_CODE", nullable = false, length = 128)
    private String ibmDepProductCode;

    @Column(name = "IBM_DEP_PRODUCT_NAME", nullable = false, length = 512)
    private String ibmDepProductName;

    @Column(name = "IBM_DEP_PRODUCT_TYPE_CODE", length = 128)
    private String ibmDepProductTypeCode;

    @Column(name = "INTEREST_PAYMENT_METHOD", length = 64)
    private String interestPaymentMethod;

    @Column(name = "ACC_CLASS_CODE", length = 128)
    private String accClassCode;

    @Column(name = "ACC_STRUCTURE_CODE", length = 64)
    private String accStructureCode;

    @Column(name = "TERM_VALUE", precision = 4)
    private Integer termValue;

    @Column(name = "TERM_UNIT", length = 32)
    private String termUnit;

    @Column(name = "INTEREST_RATE_CODE", length = 64)
    private String interestRateCode;

    @Column(name = "EARLY_INTEREST_CODE", length = 64)
    private String earlyInterestCode;

    @Column(name = "EFFECTIVE_DATE")
    private Timestamp effectiveDate;

    @Column(name = "EXPIRY_DATE")
    private Timestamp expiryDate;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
}
