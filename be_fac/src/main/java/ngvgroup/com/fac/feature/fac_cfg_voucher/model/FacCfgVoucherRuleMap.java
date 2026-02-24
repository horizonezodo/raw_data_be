package ngvgroup.com.fac.feature.fac_cfg_voucher.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_CFG_VOUCHER_RULE_MAP")
public class FacCfgVoucherRuleMap extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "PRIORITY", length = 4)
    private String priority;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "LEDGER_TYPE", nullable = false, length = 64)
    private String ledgerType;

    @Column(name = "PAYMENT_METHOD", nullable = false, length = 64)
    private String paymentMethod;

    @Column(name = "CASH_FLOW_DIR", nullable = false, length = 64)
    private String cashFlowDir;

    @Column(name = "OFFBS_DIR", nullable = false, length = 64)
    private String offbsDir;

    @Column(name = "FORCED_VOUCHER_TYPE", nullable = false, length = 64)
    private String forcedVoucherType;

    @Column(name = "VOUCHER_TYPE_CODE", nullable = false, length = 32)
    private String voucherTypeCode;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
