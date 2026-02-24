package ngvgroup.com.bpmn.dto.CtgCfgReport;

import ngvgroup.com.bpmn.dto.LabelExcelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportExcelDTO{
    private String reportGroupCode;
    private List<LabelExcelDto> label;
}
