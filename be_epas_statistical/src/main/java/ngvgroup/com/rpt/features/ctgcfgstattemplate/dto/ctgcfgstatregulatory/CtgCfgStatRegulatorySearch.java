package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatRegulatorySearch {
    private String keyword;
    private String reportModuleCode;
    private String statTypeCode;
    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private String sortDirection;
}
