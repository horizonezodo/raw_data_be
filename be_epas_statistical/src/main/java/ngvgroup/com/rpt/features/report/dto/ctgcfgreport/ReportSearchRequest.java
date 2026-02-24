package ngvgroup.com.rpt.features.report.dto.ctgcfgreport;

import lombok.Data;

import java.util.List;

@Data
public class ReportSearchRequest {
    private String reportType;
    private List<String> listGroupCode;
}
