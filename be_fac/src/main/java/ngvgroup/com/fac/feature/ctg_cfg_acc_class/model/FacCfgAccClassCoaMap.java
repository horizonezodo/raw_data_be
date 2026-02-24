package ngvgroup.com.fac.feature.ctg_cfg_acc_class.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;
@Data
@Entity
@Table(name = "FAC_CFG_ACC_CLASS_COA_MAP")
public class FacCfgAccClassCoaMap extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "EFFECTIVE_DATE", nullable = false)

    private Date effectiveDate;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Column(name = "COA_VERSION_CODE", nullable = false, length = 128)
    private String coaVersionCode;

    @Column(name = "ACC_CLASS_CODE", nullable = false, length = 128)
    private String accClassCode;

    @Column(name = "ACC_COA_CODE", nullable = false, length = 64)
    private String accCoaCode;

    @Column(name = "DEBT_GROUP_CODE", length = 32)
    private String debtGroupCode;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "CHANNEL_CODE", length = 64)
    private String channelCode;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
