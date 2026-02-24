package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatTypeResponse {
    private String statTypeCode;
    private String statTypeName;
    private String reportModuleCode;
}
