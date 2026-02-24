package ngvgroup.com.loan.feature.scoring_indc_group.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CtgCfgScoringIndcGroupDTO {
    private Long sortNumber;
    private String scoringTypeCode;
    private String scoringIndcGroupCode;
    private String scoringIndcGroupName;
    private String scoringIndcGroupType;

    public CtgCfgScoringIndcGroupDTO(
            Long sortNumber,
            String scoringTypeCode,
            String scoringIndcGroupCode,
            String scoringIndcGroupName,
            String scoringIndcGroupType
    ) {
        this.sortNumber = sortNumber;
        this.scoringTypeCode = scoringTypeCode;
        this.scoringIndcGroupCode = scoringIndcGroupCode;
        this.scoringIndcGroupName = scoringIndcGroupName;
        this.scoringIndcGroupType = scoringIndcGroupType;
    }
}
