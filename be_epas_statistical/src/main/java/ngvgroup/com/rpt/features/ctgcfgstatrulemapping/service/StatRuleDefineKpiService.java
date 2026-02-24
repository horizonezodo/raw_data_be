package ngvgroup.com.rpt.features.ctgcfgstatrulemapping.service;

import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.dto.RuleMappingDto;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.model.StatRuleDefineKpi;

import java.util.List;

public interface StatRuleDefineKpiService {
    List<StatRuleDefineKpi> saveAndUpdate(RuleMappingDto dto);
    List<StatRuleDefineKpi> getList(String templateCode,String kpiCode);
    void deleteByTemplateCodeAndKpiCode(String templateCode,String kpiCode);
}
