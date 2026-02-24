package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgWorkflowDto {
    private Long id;
    private String workflowCode;
    private String workflowName;
    private String initialStatusCode;
    private BigDecimal versionNo;
    private String recordStatus;
    private String description;

    // Constructor for query projection
    public CtgCfgWorkflowDto(String workflowCode, String workflowName, String initialStatusCode, BigDecimal versionNo) {
        this.workflowCode = workflowCode;
        this.workflowName = workflowName;
        this.initialStatusCode = initialStatusCode;
        this.versionNo = versionNo;
    }
}
