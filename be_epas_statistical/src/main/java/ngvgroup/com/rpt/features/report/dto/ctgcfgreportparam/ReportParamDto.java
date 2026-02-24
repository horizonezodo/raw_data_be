package ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportParamDto {
    private Long id;
    private String reportCode;
    private String parameterCode;
    private String parameterName;
    private String parameterType;
    private String controlType;
    private String resourceSql;
}
