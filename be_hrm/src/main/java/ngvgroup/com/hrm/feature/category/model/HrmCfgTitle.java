package ngvgroup.com.hrm.feature.category.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "HRM_CFG_TITLE")
@Getter
@Setter
public class HrmCfgTitle extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "TITLE_CODE", nullable = false, length = 32)
    private String titleCode;

    @Column(name = "TITLE_NAME", nullable = false, length = 128)
    private String titleName;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;
}
