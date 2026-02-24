package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatesheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatTemplateSheetDTO {
    private Long id;
    private String recordStatus;
    private String orgCode;
    private String templateCode;
    private String sheetData;
    private double areaId;
    private double columnStart;
    private double rowStart;
    private double columnEnd;
    private double rowEnd;
    private double rowToDelete;
    private String tableData;
}
