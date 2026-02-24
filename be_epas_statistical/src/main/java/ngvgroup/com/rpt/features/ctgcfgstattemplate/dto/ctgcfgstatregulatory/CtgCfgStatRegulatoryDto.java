package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CtgCfgStatRegulatoryDto {
    private Long id;
    private String orgCode;
    private String statRegulatoryCode;
    private String statRegulatoryName;
    private String statTypeCode;
    private String statTypeName;
    private String reportModuleCode;
    private String recordStatus;
    private Integer isActive;
    private Long sortNumber;
    private String description;
    private String commonCode;
    private String commonName;
}
