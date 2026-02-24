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
public class ComCfgSlaTaskDtlDto {

    private String orgCode;
    private String taskDefineCode;
    private String processDefineCode;
    private Date effectiveDate;
    private Double slaMaxDuration;
    private Double slaWarningDuration;
    private Double slaWarningPercent;


    public ComCfgSlaTaskDtlDto(String orgCode, String taskDefineCode, String processDefineCode, Date effectiveDate, Double slaMaxDuration, Double slaWarningDuration, Double slaWarningPercent) {
        this.orgCode = orgCode;
        this.taskDefineCode = taskDefineCode;
        this.processDefineCode = processDefineCode;
        this.effectiveDate = effectiveDate;
        this.slaMaxDuration = slaMaxDuration;
        this.slaWarningDuration = slaWarningDuration;
        this.slaWarningPercent = slaWarningPercent;
    }

    private String priorityLevel;
    private String slaWarningType;


}

