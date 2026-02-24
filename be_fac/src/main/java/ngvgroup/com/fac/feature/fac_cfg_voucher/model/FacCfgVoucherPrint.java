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
@Table(name = "FAC_CFG_VOUCHER_PRINT")
public class FacCfgVoucherPrint extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "VOUCHER_TYPE_CODE", nullable = false, length = 32)
    private String voucherTypeCode;

    @Column(name = "PRINT_MODE", length = 64)
    private String printMode;

    @Column(name = "TEMPLATE_CODE", nullable = false, length = 128)
    private String templateCode;

    @Column(name = "TEMPLATE_NAME", nullable = false, length = 256)
    private String templateName;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
