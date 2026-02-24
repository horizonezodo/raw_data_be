package ngvgroup.com.bpm.features.sla.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UpdateSlaProcessDtlCmd {

    private String orgCode;
    private String processDefineCode;

    private String slaWarningType;
    private Double slaMaxDuration;
    private Double slaWarningPercent;
    private Double slaWarningDuration;
    private Date effectiveDate;
    private String isActive;
}
