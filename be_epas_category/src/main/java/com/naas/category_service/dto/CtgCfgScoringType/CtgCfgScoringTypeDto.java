package com.naas.category_service.dto.CtgCfgScoringType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgScoringTypeDto {
    private String scoringTypeCode;
    private String scoringTypeName;
    private String description;

    private String sqlDataCollection;
    private String templateCollectionCode;
    private String sqlCalcResult;
    private String templateResultCode;


    public CtgCfgScoringTypeDto(String scoringTypeCode, String scoringTypeName) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
    }

    public CtgCfgScoringTypeDto(String scoringTypeCode, String scoringTypeName, String description) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
        this.description = description;
    }
}
