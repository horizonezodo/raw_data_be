package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatesheet.CtgCfgStatTemplateSheetDTO;

import java.util.List;

public interface CtgCfgStatTemplateSheetService {
    void createTemplateSheet(List<CtgCfgStatTemplateSheetDTO> dtos,String templateCode);
    void updateTemplateSheet(List<CtgCfgStatTemplateSheetDTO> dtos,String templateCode);
    void deleteTemplateSheet(String templateCode);

    List<CtgCfgStatTemplateSheetDTO> getAllTemplateSheetByTemplateCode(String templateCode);
}
