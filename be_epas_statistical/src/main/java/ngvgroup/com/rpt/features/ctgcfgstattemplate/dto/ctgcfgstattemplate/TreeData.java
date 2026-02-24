package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeData {
    private String circularName;
    private String circularCode;
    private String templateGroupName;
    private String templateGroupCode;
    List<CtgCfgStatTemplateDtoV2> ctgCfgStatTemplateDtos;
    private boolean isChecked;
}
