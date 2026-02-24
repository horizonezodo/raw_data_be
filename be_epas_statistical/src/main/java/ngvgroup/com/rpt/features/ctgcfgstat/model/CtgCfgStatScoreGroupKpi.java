package ngvgroup.com.rpt.features.ctgcfgstat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "CTG_CFG_STAT_SCORE_GROUP_KPI")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatScoreGroupKpi extends BaseEntity {
    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "STAT_SCORE_GROUP_CODE", nullable = false, length = 64)
    private String statScoreGroupCode;

    @Column(name = "KPI_CODE", nullable = false, length = 32)
    private String kpiCode;

    @Column(name = "WEIGHT_SCORE",precision = 7,scale = 4)
    private BigDecimal weightScore;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
