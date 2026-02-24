package ngvgroup.com.bpm.features.sla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgSlaProcessDtlDto {

    private String slaWarningType;
    private Double slaMaxDuration;
    private Double slaWarningPercent;
    private Double slaWarningDuration;
    private Date effectiveDate;
    private String processDefineCode;
    private String orgCode;

    public ComCfgSlaProcessDtlDto(String slaWarningType, Double slaMaxDuration, Double slaWarningPercent, Double slaWarningDuration, Date effectiveDate, String processDefineCode, String orgCode) {
        this.slaWarningType = slaWarningType;
        this.slaMaxDuration = slaMaxDuration;
        this.slaWarningPercent = slaWarningPercent;
        this.slaWarningDuration = slaWarningDuration;
        this.effectiveDate = effectiveDate;
        this.processDefineCode = processDefineCode;
        this.orgCode = orgCode;
    }

    private  String isActive;
}

