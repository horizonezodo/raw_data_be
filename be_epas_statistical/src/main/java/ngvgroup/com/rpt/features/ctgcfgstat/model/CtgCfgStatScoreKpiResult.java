package ngvgroup.com.rpt.features.ctgcfgstat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "CTG_CFG_STAT_SCORE_KPI_RESULT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatScoreKpiResult extends BaseEntity {
    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "KPI_CODE", length = 32)
    private String kpiCode;

    @Column(name = "RESULT_NAME", length = 128)
    private String resultName;

    @Column(name = "SCORE_VALUE")
    private BigDecimal scoreValue;

    @Column(name = "SCORE_VALUE_MIN")
    private BigDecimal scoreValueMin;

    @Column(name = "SCORE_VALUE_MAX")
    private BigDecimal scoreValueMax;

    @Column(name = "CONDITION_EXPRESSION", length = 256)
    private String conditionExpression;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
