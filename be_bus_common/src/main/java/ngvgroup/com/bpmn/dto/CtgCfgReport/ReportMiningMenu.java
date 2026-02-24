package ngvgroup.com.bpmn.dto.CtgCfgReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportMiningMenu {
    private String reportGroupName;
    private List<ReportDto> reports;
}
