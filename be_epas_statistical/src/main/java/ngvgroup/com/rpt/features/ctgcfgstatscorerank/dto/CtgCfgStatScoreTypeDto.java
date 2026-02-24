package ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatScoreTypeDto {
    private String statScoreTypeCode;
    private String statScoreTypeName;
    private String description;

    private String templateResultCode;
    private String sqlCalcResult;
    private String orgCode;
    private String recordStatus;
    private Long id;


    public CtgCfgStatScoreTypeDto(String statScoreTypeCode, String statScoreTypeName, String description,Long id) {
        this.statScoreTypeCode = statScoreTypeCode;
        this.statScoreTypeName = statScoreTypeName;
        this.description = description;
        this.id=id;
    }
}
