package ngvgroup.com.bpmn.dto.CtgCfgReport;

import lombok.Data;
import java.util.List;

@Data
public class ReportSearchRequest {
    private String reportType;
    private List<String> listGroupCode;
}
