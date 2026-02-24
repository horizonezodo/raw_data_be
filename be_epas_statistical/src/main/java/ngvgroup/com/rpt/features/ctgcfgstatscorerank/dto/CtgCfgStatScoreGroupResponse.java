package ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatScoreGroupResponse {
    private String orgCode;
    private String statScoreGroupName;
    private String statScoreTypeName;
    private String description;
    private Integer isActive;
    private String statScoreGroupCode;
    private String statScoreTypeCode;
    private String recordStatus;
    private Long sortNumber;
    private String statScoreGroupTypeCode;
    private String statScoreGroupTypeName;
    private BigDecimal weightScore;
}
