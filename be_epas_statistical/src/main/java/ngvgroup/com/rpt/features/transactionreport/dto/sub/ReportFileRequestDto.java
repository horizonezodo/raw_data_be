package ngvgroup.com.rpt.features.transactionreport.dto.sub;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ReportFileRequestDto {
    private String instanceCode;
    private String guiStatus;
    private Map<String, List<ExcelChangeIndexDto>> listChanges; // sheetIdx: listChange
}
