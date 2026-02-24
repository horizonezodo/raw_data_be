package ngvgroup.com.bpmn.dto.CtgCfgReport;

import ngvgroup.com.bpmn.dto.CtgCfgReportGroup.CtgCfgReportGroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportMenu {
    private String commonCode;
    private String commonName;
    private List<CtgCfgReportGroupDto> reportGroup;
}
