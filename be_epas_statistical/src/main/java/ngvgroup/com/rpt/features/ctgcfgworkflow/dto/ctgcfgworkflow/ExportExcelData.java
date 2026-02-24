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
public class ExportExcelData {
    private String workflowCode;
    private String workflowName;
    private String initialStatusCode;
    private BigDecimal versionNo;
}
