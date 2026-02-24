package ngvgroup.com.rpt.features.ctgcfgstatus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelData {
    private String statusCode;
    private String statusName;
    private String statusTypeCode;
    private String statusTypeName;
    private String description;
}
