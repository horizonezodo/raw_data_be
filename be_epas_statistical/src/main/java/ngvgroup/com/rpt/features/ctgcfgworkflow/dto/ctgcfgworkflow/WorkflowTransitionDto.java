package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransitionDto {
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
    private String workflowCode;

    // Danh sách điều kiện hành động
    private List<TransitionConditionDto> conditions;

    // Danh sách hậu xử lý chuyển trạng thái
    private List<TransitionPostFunctionDto> postFunctions;
}
