package ngvgroup.com.rpt.features.report.dto.ctgcfgreport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.rpt.features.report.dto.LabelExcelDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportExcelDTO{
    private String reportGroupCode;
    private List<LabelExcelDto> label;
}
