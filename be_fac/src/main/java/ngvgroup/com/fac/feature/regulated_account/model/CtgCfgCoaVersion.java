package ngvgroup.com.fac.feature.regulated_account.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Table(name = "FAC_CFG_COA_VERSION")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CtgCfgCoaVersion extends BaseEntity {
    @Column(name = "COA_VERSION_CODE", length = 128, nullable = false)
    private String coaVersionCode;
    @Column(name = "COA_VERSION_NAME", length = 256, nullable = false)
    private String coaVersionName;
    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;
    @Column(name = "COA_SCOPE", length = 64, nullable = false)
    private String coaScope;
    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Timestamp effectiveDate;
    @Column(name = "EXPIRE_DATE")
    private Timestamp expireDate;
    @Column(name = "IS_DEFAULT")
    private int isDefault;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;

}
