package ngvgroup.com.loan.feature.scoring_indc_result.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_SCORING_INDC_RESULT")
public class CtgCfgScoringIndcResult extends BaseEntity {
    @Column(name = "SORT_NUMBER", length = 22)
    private int sortNumber;
    @Column(name = "INDICATOR_CODE", length = 128)
    private String indicatorCode;
    @Column(name = "RESULT_NAME", length = 128)
    private String resultName;
    @Column(name = "SCORE_VALUE", precision = 7,scale = 4)
    private BigDecimal scoreValue;
    @Column(name = "SCORE_VALUE_MIN", precision = 7,scale = 4)
    private BigDecimal scoreValueMin;
    @Column(name = "SCORE_VALUE_MAX", precision = 7,scale = 4)
    private BigDecimal scoreValueMax;
    @Column(name = "CONDITION_EXPRESSION", length = 256)
    private String conditionExpression;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
