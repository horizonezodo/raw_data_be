package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatFilterTreeItem {
    private String reportModuleCode;
    private String commonCode;
    private String commonName;
    private String statTypeCode;
    private String statTypeName;
}
