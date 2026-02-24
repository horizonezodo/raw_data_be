package ngvgroup.com.rpt.features.ctgcfgstatus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatusDto {
    private Long id;
    private Long sortNumber;
    private String statusCode;
    private String statusName;
    private String statusTypeCode;
    private String statusTypeName;
    private Integer isFinal;
    private String description;
    private String recordStatus;
    private Integer isActive;
    private Integer isAggregation;
    private Integer isAllowEdit;
    private Integer isCheck;

    private CtgCfgStatusSlaDto slaDto;

    // Constructor for query projection
    public CtgCfgStatusDto(String statusCode, String statusName, String statusTypeCode, String statusTypeName,
                           String description, Integer isActive) {
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.statusTypeCode = statusTypeCode;
        this.statusTypeName = statusTypeName;
        this.description = description;
        this.isActive = isActive;
    }
}
