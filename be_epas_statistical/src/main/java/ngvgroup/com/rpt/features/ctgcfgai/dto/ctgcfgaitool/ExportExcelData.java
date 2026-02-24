package ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelData {
    private String toolAiCode;
    private String toolAiName;
    private String toolAiTypeCode;
    private String toolAiTypeName;
    private String description;
}
