package ngvgroup.com.fac.feature.fac_cfg_acct_entry.model;

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
@Table(name = "FAC_CFG_ACCT_PROCESS")
public class FacCfgAcctProcess extends BaseEntity {
    private Long id;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "ACCT_PROCESS_CODE", nullable = false, length = 64)
    private String acctProcessCode;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", nullable = false, length = 256)
    private String processTypeName;

    @Column(name = "MODULE_CODE", nullable = false, length = 64)
    private String moduleCode;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
