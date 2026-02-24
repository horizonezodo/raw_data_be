package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.CtgCfgStatTemplateKpiDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiRequestDto;

import java.util.List;

public interface CtgCfgStatTemplateKpiService {
    void createTemplateKpi(List<CtgCfgStatTemplateKpiDTO> dtos,String templateCode);
    void updateTemplateKpi(List<CtgCfgStatTemplateKpiDTO> dtos,String templateCode);

    List<CtgCfgStatTemplateKpiDTO> getAllByTemplateCode(String templateCode);

    void deleteTemplateKpi(String templateCode);
    List<IndexKpiDto> getKpiByIndex(List<IndexKpiRequestDto> request);

    CtgCfgStatTemplateKpiDTO getByTemplateKpiCode(String templateKpiCode);
}
