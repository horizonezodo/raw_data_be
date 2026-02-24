package ngvgroup.com.rpt.features.ctgcfgstatkpi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatKpiDto {
    private Long id;
    private String kpiCode;
    private String kpiName;
    private String description;

    private String expressionSql;
    private String agrtTimingName;
    private String agrtTimingCode;
    private Integer sortNumber;
    private Boolean isValid;
    private String validSql;
    private String kpiTypeCode;
    private String kpiTypeName;
    private String parentCode;
    private String orgCode;
    private String recordStatus;

    public CtgCfgStatKpiDto(String kpiCode, String kpiName, String description,Long id) {
        this.id=id;
        this.kpiCode = kpiCode;
        this.kpiName = kpiName;
        this.description = description;
    }

    public CtgCfgStatKpiDto(String kpiCode, String kpiName) {
        this.kpiCode = kpiCode;
        this.kpiName = kpiName;
    }
}
