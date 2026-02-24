package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.workflowtransition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelData {
    private String workflowCode;
    private String transitionCode;
    private String transitionName;
    private String fromStatusName;
    private String toStatusName;
    private Integer sortNumber;
}
