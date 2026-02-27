package ngvgroup.com.loan.feature.scoring_indc_group.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_SCORING_INDC_GROUP")
public class CtgCfgScoringIndcGroup extends BaseEntity {
    @Column(name = "SORT_NUMBER", length = 22)
    private BigInteger sortNumber;
    @Column(name = "SCORING_TYPE_CODE", length = 128, nullable = false)
    private String scoringTypeCode;
    @Column(name = "SCORING_INDC_GROUP_CODE", length = 128, nullable = false)
    private String scoringIndcGroupCode;
    @Column(name = "SCORING_INDC_GROUP_NAME", length = 256)
    private String scoringIndcGroupName;
    @Column(name = "SCORING_INDC_GROUP_TYPE", length = 128)
    private String scoringIndcGroupType;
    @Column(name = "WEIGHT_SCORE", precision = 7, scale = 4)
    private BigDecimal weightScore;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    public CtgCfgScoringIndcGroup(String scoringTypeCode, String scoringIndcGroupCode, String scoringIndcGroupName, String scoringIndcGroupType, BigDecimal weightScore, BigInteger sortNumber, String description) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringIndcGroupCode = scoringIndcGroupCode;
        this.scoringIndcGroupName = scoringIndcGroupName;
        this.scoringIndcGroupType = scoringIndcGroupType;
        this.weightScore = weightScore;
        this.sortNumber = sortNumber;
        this.setIsActive(1);
        this.setDescription(description);

    }
}
