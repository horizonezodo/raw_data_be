package ngvgroup.com.fac.feature.fac_cfg_acct_entry.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_CFG_ACCT_ENTRY_DTL")
public class FacCfgAcctEntryDtl extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "ENTRY_SEQ_NO", nullable = false, length = 2)
    private Integer entrySeqNo;

    @Column(name = "ENTRY_CODE", nullable = false, length = 128)
    private String entryCode;

    @Column(name = "ACC_SIDE_TYPE", nullable = false, length = 128)
    private String accSideType;

    @Column(name = "ENTRY_TYPE", nullable = false, length = 64)
    private String entryType;

    @Column(name = "ACC_CLASS_CODE", length = 128)
    private String accClassCode;

    @Column(name = "AMT_TYPE", nullable = false, length = 128)
    private String amtType;

    @Column(name = "AMT_PARAM_CODE", nullable = false, length = 128)
    private String amtParamCode;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
