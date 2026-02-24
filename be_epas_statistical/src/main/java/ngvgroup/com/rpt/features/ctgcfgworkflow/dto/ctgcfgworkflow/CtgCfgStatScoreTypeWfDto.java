package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatScoreTypeWfDto {
    private Long id;
    private String orgCode;
    private String statScoreTypeCode;
    private String workflowCode;
    private Integer versionNo;
}
