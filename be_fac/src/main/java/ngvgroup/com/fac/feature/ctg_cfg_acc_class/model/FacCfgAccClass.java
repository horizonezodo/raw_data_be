package ngvgroup.com.fac.feature.ctg_cfg_acc_class.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "FAC_CFG_ACC_CLASS")
public class FacCfgAccClass extends BaseEntity {

    @Column(name = "ACC_CLASS_CODE", nullable = false, length = 128)
    private String accClassCode;

    @Column(name = "ACC_CLASS_NAME", nullable = false, length = 256)
    private String accClassName;

    @Column(name = "ACC_SIDE_TYPE", nullable = false, length = 128)
    private String accSideType;

    @Column(name = "ACC_NATURE", nullable = false, length = 128)
    private String accNature;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
