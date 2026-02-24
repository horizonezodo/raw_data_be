package ngvgroup.com.hrm.feature.category.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "HRM_CFG_POSITION")
@Getter
@Setter
public class HrmCfgPosition extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64, columnDefinition = "varchar2(64) default '%'")
    private String orgCode;

    @Column(name = "POSITION_CODE", nullable = false, length = 32)
    private String positionCode;

    @Column(name = "POSITION_NAME", nullable = false, length = 128)
    private String positionName;

    @Column(name = "IS_MANAGER", nullable = false)
    private Integer isManager;

    @Column(name = "ORG_LEVEL_CODE", nullable = false, length = 64)
    private String orgLevelCode;

    @Column(name = "TITLE_CODE", length = 32)
    private String titleCode;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;
}
