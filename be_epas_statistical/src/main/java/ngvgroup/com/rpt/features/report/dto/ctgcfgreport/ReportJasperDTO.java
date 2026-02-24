package ngvgroup.com.rpt.features.report.dto.ctgcfgreport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportJasperDTO {
    private String reportCode;
    private String format;
    private Map<String, Object> params;
}
