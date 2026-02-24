package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatTemplateDtoV2 extends CtgCfgStatTemplateDtoV1 {
    private String templateCode;
    private String templateName;

    public CtgCfgStatTemplateDtoV2(
            String templateGroupCode,
            String templateGroupName,
            String templateCode,
            String templateName
    ) {
        super(templateGroupCode, templateGroupName);
        this.templateCode = templateCode;
        this.templateName = templateName;
    }
}
