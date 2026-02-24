package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.workflowtransition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.rpt.features.ctgcfgtransition.dto.CtgCfgTransitionCondDto;
import ngvgroup.com.rpt.features.ctgcfgtransition.dto.CtgCfgTransitionPostFuncDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransitionDto {
    private Long id;
    private String workflowCode;
    private String transitionCode;
    private String transitionName;
    private String fromStatusCode;
    private String fromStatusName;
    private String toStatusCode;
    private String toStatusName;
    private Integer isGlobal;
    private Integer sortNumber;
    private Integer isAllowComment;
    private Integer isAllowAttachment;
    private String description;
    private String recordStatus;

    private List<CtgCfgTransitionCondDto> conditions;
    private List<CtgCfgTransitionPostFuncDto> postFunctions;

}