package ngvgroup.com.loan.feature.scoring_indc_group.dto;

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
public class CtgCfgScoringIndcGroupDtoV2 {

    private String scoringIndcGroupCode;
    private String scoringIndcGroupName;
    private BigDecimal weightScore;
    private BigInteger sortNumber;

    private String scoringTypeCode;
    private String scoringIndcGroupType;
    private String description;

    public CtgCfgScoringIndcGroupDtoV2(String scoringIndcGroupCode, String scoringIndcGroupName, BigInteger sortNumber, String scoringTypeCode, String scoringIndcGroupType) {
        this.scoringIndcGroupCode = scoringIndcGroupCode;
        this.scoringIndcGroupName = scoringIndcGroupName;
        this.sortNumber = sortNumber;
        this.scoringTypeCode = scoringTypeCode;
        this.scoringIndcGroupType = scoringIndcGroupType;
    }
}
