package ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroupdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgReportGroupDTO {
    private Long id;
    private String description;
    private String reportGroupCode;
    private String reportGroupName;
    private String reportGroupNameEn;
}