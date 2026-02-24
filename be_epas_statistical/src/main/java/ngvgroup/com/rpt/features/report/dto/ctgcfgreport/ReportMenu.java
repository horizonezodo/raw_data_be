package ngvgroup.com.rpt.features.report.dto.ctgcfgreport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportMenu {
    private String commonCode;
    private String commonName;
    private List<CtgCfgReportGroupDto> reportGroup;
}
