package ngvgroup.com.hrm.feature.hrm_cfg_status.model;

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
@Table(name = "HRM_CFG_STATUS")
public class HrmCfgStatus extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "STATUS_CODE", nullable = false, length = 64)
    private String statusCode;

    @Column(name = "STATUS_NAME", nullable = false, length = 128)
    private String statusName;
}
