package ngvgroup.com.bpmn.dto.CtgCfgReportParamCond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportParamCondDTO {
    private Long id;
    private String reportCode;
    private String sourceParamCode;
    private String sourceParamName;
    private String sourceParamValue;
    private String sourceParamValueName;
    private String targetParamCode;
    private String targetParamName;
    private String conditionType;
    private String expression;
    private Long sortNumber;
    private String description;

}
