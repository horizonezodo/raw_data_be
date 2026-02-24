package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.CtgCfgStatTemplateKpiDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatesheet.CtgCfgStatTemplateSheetDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFormDTO {
    private CtgCfgStatTemplateDto ctgCfgStatTemplateDto;
    private List<CtgCfgStatTemplateSheetDTO> ctgCfgStatTemplateSheetDTOS;
    private List<CtgCfgStatTemplateKpiDTO> ctgCfgStatTemplateKpiDTOS;
    private CtgCfgStatTemplateDeadLineDTO ctgCfgStatTemplateDeadLineDTOS;
}
