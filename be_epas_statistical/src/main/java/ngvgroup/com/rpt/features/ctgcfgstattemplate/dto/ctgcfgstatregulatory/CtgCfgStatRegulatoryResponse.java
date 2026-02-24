package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatRegulatoryResponse {
    private String statRegulatoryCode;
    private String statRegulatoryName;
    private String reportModuleCode;
    private String commonName;
    private String statTypeCode;
    private String statTypeName;
    private Integer isActive;
    private String recordStatus;
    private Long sortNumber;
    private String description;
}
