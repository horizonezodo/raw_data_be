package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelData {
    private String templateCode;
    private String templateName;
    private String templateGroupCode;
    private String description;
    private String frequency;
}
