package ngvgroup.com.fac.feature.regulated_account.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FAC_CFG_COA_ACC")
@Entity
public class CtgCfgCoaAcc extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "COA_VERSION_CODE", length = 128, nullable = false)
    private String coaVersionCode;
    @Column(name = "COA_SCOPE", length = 64, nullable = false)
    private String coaScope;
    @Column(name = "ACC_COA_CODE", length = 64, nullable = false)
    private String accCoaCode;
    @Column(name = "ACC_COA_NAME", length = 128, nullable = false)
    private String accCoaName;
    @Column(name = "IS_INTERNAL", length = 32, nullable = false)
    private String isInternal;
    @Column(name = "IS_LEAF", length = 1, nullable = false)
    private int isLeaf;
    @Column(name = "IS_ALLOW_POSTING", length = 1, nullable = false)
    private int isAllowPosting;
    @Column(name = "EFFECTIVE_DATE")
    private Timestamp effectiveDate;
    @Column(name = "EXPIRY_DATE")
    private Timestamp expiryDate;
    @Column(name = "ACC_STATUS", length = 64, nullable = false)
    private String accStatus;
    @Column(name = "PARENT_CODE", length = 64)
    private String parentCode;
    @Column(name = "ACC_LEVEL", length = 2, nullable = false)
    private int accLevel;
    @Column(name = "ACC_SUB_LEVEL", length = 2, nullable = false)
    private int accSubLevel;
    @Column(name = "ACC_TYPE", length = 64, nullable = false)
    private String accType;
    @Column(name = "ACC_NATURE", length = 8, nullable = false)
    private String accNature;
    @Column(name = "ACC_SCOPE", length = 16, nullable = false)
    private String accScope;
    @Column(name = "ACC_COA_MAP", length = 64)
    private String accCoaMap;
    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";
}
