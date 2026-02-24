package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterReportRuleDto {
    private String regulatoryTypeCode;
    private String commonCode;
    private List<String> templateGroupCodes;
    private List<String> circularCodes;
    private List<String> defaultCircularCodes;
}
