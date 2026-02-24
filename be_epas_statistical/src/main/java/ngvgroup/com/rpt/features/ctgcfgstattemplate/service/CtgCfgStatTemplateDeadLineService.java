package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO;

public interface CtgCfgStatTemplateDeadLineService {
    void createDeadLine(CtgCfgStatTemplateDeadLineDTO dto, String templateCode);
    void updateDeadLine(CtgCfgStatTemplateDeadLineDTO dto, String templateCode);
     CtgCfgStatTemplateDeadLineDTO getAllByTemplateCode(String templateCode);
     void deleteAllByTemplateCode(String templateCode);
}
