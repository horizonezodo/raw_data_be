package ngvgroup.com.bpm.features.sla.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComCfgSlaProcessDto {
    private String orgCode;
    private String processTypeCode;
    private String slaType;
    private String unit;
    private Integer isActive;
    private String processDefineCode;

    public ComCfgSlaProcessDto(String orgCode, String processTypeCode, String slaType, String unit, Integer isActive, String processDefineCode) {
        this.orgCode = orgCode;
        this.processTypeCode = processTypeCode;
        this.slaType = slaType;
        this.unit = unit;
        this.isActive = isActive;
        this.processDefineCode = processDefineCode;
    }
}

