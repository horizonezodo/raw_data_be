package ngvgroup.com.rpt.features.report.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_CFG_REPORT_PARAM_BASE")
public class CtgCfgReportParamBase extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "PARAM_BASE_CODE", nullable = false, length = 128)
    private String paramBaseCode;

    @Column(name = "PARAM_BASE_NAME", nullable = false, length = 256)
    private String paramBaseName;

    @Column(name = "PARAM_BASE_TYPE", nullable = false, length = 16)
    private String paramBaseType;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
