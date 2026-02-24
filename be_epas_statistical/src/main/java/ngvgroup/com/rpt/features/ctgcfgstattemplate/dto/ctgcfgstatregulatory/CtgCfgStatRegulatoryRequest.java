package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatRegulatoryRequest {
    private Long id;
    private String statRegulatoryCode;
    private String statRegulatoryName;
    private String reportModuleCode;
    private String commonCode;
    private String statTypeCode;
    private String statTypeName;
    private Integer isActive;
    private Integer sortNumber;
    private String description;
    private String recordStatus;
}
