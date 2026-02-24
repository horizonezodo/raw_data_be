package ngvgroup.com.rpt.features.ctgcfgstatkpi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_TYPE_KPI")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatTypeKpi extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "KPI_TYPE_CODE", nullable = false, length = 64)
    private String kpiTypeCode;

    @Column(name = "KPI_TYPE_NAME", nullable = false, length = 128)
    private String kpiTypeName;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
