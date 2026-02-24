package ngvgroup.com.bpmn.dto.CtgCfgReportParamCond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgReportParamCondDTO {
    private Long id;
    private String reportCode;
    private String sourceParamCode;
    private String sourceParamValue;
    private String targetParamCode;
    private String conditionType;
    private String expression;
    private Long sortNumber;
    private String description;
}