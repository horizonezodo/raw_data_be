package ngvgroup.com.bpmn.dto.CtgCfgReportParamBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgReportParamBaseDto {

    private String paramBaseCode;
    private String paramBaseName;
    private String orgCode;
    private String paramBaseType;
    private String description;
    private String orgName;
}
