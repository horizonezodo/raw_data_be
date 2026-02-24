package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexKpiDto {
    private String templateCode;
    private String index;
    private String kpiCode;
    private String templateKpiCode;
    private String templateKpiName;
}
