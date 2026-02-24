package ngvgroup.com.fac.feature.fac_cfg_acct_entry.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_CFG_ACCT_ENTRY")
public class FacCfgAcctEntry extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "ACCT_PROCESS_CODE", nullable = false, length = 64)
    private String acctProcessCode;

    @Column(name = "ENTRY_TYPE_CODE", nullable = false, length = 128)
    private String entryTypeCode;

    @Column(name = "ENTRY_CODE", length = 128)
    private String entryCode;

    @Column(name = "ENTRY_NAME", nullable = false, length = 256)
    private String entryName;

    @Column(name = "ENTRY_SEQ_NO", nullable = false, length = 2)
    private Integer entrySeqNo;

    @Column(name = "LEDGER_TYPE", nullable = false, length = 64)
    private String ledgerType;

    @Column(name = "ENTRY_DIR", nullable = false, length = 64)
    private String entryDir;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
