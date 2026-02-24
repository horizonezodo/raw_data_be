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
@Table(name = "FAC_CFG_VOUCHER_METHOD_GEN")
public class FacCfgVoucherMethodGen extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", nullable = false, length = 256)
    private String processTypeName;

    @Column(name = "VOUCHER_GEN_MODE", nullable = false, length = 64)
    private String voucherGenMode;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
