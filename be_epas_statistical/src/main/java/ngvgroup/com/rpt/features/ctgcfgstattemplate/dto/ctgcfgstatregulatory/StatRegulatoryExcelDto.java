package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatRegulatoryExcelDto {
    private String statRegulatoryCode;
    private String statRegulatoryName;
    private String statTypeCode;
    private String reportModuleCode;

}
