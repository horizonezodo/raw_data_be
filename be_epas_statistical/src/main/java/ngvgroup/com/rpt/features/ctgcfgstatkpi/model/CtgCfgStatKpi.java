package ngvgroup.com.rpt.features.ctgcfgstatkpi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_KPI")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatKpi extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "KPI_CODE", nullable = false, length = 32)
    private String kpiCode;

    @Column(name = "KPI_NAME", nullable = false, length = 512)
    private String kpiName;

    @Column(name = "KPI_TYPE_CODE", nullable = false, length = 64)
    private String kpiTypeCode;

    @Column(name = "KPI_TYPE_NAME", nullable = false, length = 128)
    private String kpiTypeName;

    @Column(name = "EXPRESSION_SQL", length = 4000)
    private String expressionSql;

    @Column(name = "AGRT_TIMING_CODE", length = 32)
    private String agrtTimingCode;

    @Column(name = "AGRT_TIMING_NAME", length = 128)
    private String agrtTimingName;

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "IS_VALID")
    private Boolean isValid;

    @Column(name = "VALID_SQL", length = 4000)
    private String validSql;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;


}
