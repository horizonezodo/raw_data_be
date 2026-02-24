package ngvgroup.com.hrm.feature.cfg_org_unit.model;

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
@Table(name = "HRM_CFG_ORG_UNIT")
public class HrmCfgOrgUnit extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode = "%";

    @Column(name = "ORG_UNIT_CODE", nullable = false, length = 64)
    private String orgUnitCode;

    @Column(name = "ORG_UNIT_NAME", nullable = false, length = 128)
    private String orgUnitName;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "UNIT_TYPE_CODE", nullable = false, length = 64)
    private String unitTypeCode;

    @Column(name = "ORG_LEVEL_CODE", nullable = false, length = 64)
    private String orgLevelCode;
}
