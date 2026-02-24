package ngvgroup.com.rpt.features.ctgcfgstatrulemapping.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.dto.RuleMappingDto;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.model.StatRuleDefineKpi;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.repository.StatRuleDefineKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.service.StatRuleDefineKpiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatRuleDefineKpiServiceImpl implements StatRuleDefineKpiService {

    private final StatRuleDefineKpiRepository repository;

    @Override
    public List<StatRuleDefineKpi> saveAndUpdate(RuleMappingDto dto) {

        repository.deleteByTemplateCodeAndKpiCode(dto.getTemplateCode(), dto.getKpiCode());

        List<StatRuleDefineKpi> list = new ArrayList<>();
        for (String ruleCode : dto.getRuleCodes()) {
            StatRuleDefineKpi kpi = new StatRuleDefineKpi();
            kpi.setRuleCode(ruleCode);
            kpi.setTemplateCode(dto.getTemplateCode());
            kpi.setKpiCode(dto.getKpiCode());
            kpi.setRecordStatus("approval");
            list.add(kpi);
        }

        return repository.saveAll(list);
    }

    @Override
    public List<StatRuleDefineKpi> getList(String templateCode,String kpiCode) {
        return repository.findAllByTemplateCodeAndKpiCode(templateCode, kpiCode);
    }

    @Override
    public void deleteByTemplateCodeAndKpiCode(String templateCode,String kpiCode){

        repository.deleteByTemplateCodeAndKpiCode(templateCode,kpiCode);
    }

}
