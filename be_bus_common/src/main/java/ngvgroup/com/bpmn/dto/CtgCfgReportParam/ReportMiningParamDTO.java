package ngvgroup.com.bpmn.dto.CtgCfgReportParam;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMiningParamDTO {
    private Long id;
    private String reportCode;
    private String parameterCode;
    private String parameterName;
    private String parameterNameEn;
    private String parameterType;
    private String controlType;
    private String resourceSql;
    private Integer isDisplay;
    private Long sortNumber;
    private String displayFrame;
    private List<ReportParamCondDTO> reportParamConds;
}
