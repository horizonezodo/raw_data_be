package ngvgroup.com.loan.feature.scoring_type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgScoringTypeDTO {
    private String scoringTypeCode;
    private String scoringTypeName;
    private String description;
    private String sqlDataCollection;
    private String templateCollectionCode;
    private String sqlCalcResult;
    private String templateResultCode;

    public CtgCfgScoringTypeDTO(String scoringTypeCode, String scoringTypeName) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
    }

    public CtgCfgScoringTypeDTO(String scoringTypeCode, String scoringTypeName, String sqlDataCollection, String templateCollectionCode, String sqlCalcResult, String templateResultCode) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
        this.sqlDataCollection = sqlDataCollection;
        this.templateCollectionCode = templateCollectionCode;
        this.sqlCalcResult = sqlCalcResult;
        this.templateResultCode = templateResultCode;
    }


    public CtgCfgScoringTypeDTO(String scoringTypeCode, String scoringTypeName, String description) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
        this.description = description;
    }
}
