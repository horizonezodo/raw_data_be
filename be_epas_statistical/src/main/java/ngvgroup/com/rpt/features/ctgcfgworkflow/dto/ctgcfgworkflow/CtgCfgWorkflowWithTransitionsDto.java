package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgWorkflowWithTransitionsDto {
    private Long id;
    private String workflowCode;
    private String workflowName;
    private String initialStatusCode;
    private BigDecimal versionNo;
    private String recordStatus;
    private String description;

    // Danh sách transitions với đầy đủ thông tin
    private List<WorkflowTransitionDto> transitions;
}
