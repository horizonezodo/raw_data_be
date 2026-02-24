package com.naas.category_service.dto.CtgCfgScoringIndcGroup;

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
public class CtgCfgScoringIndcGroupDto {

    private String scoringIndcGroupCode;
    private String scoringIndcGroupName;
    private BigDecimal weightScore;
    private BigInteger sortNumber;

    private String scoringTypeCode;
    private String scoringIndcGroupType;
    private String description;

}
