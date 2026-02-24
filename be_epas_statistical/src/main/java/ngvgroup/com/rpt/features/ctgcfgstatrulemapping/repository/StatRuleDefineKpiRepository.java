package ngvgroup.com.rpt.features.ctgcfgstatrulemapping.repository;

import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.model.StatRuleDefineKpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StatRuleDefineKpiRepository extends JpaRepository<StatRuleDefineKpi, Integer> {
    @Transactional
    void deleteByTemplateCodeAndKpiCode(String templateCode, String kpiCode);
    List<StatRuleDefineKpi> findAllByTemplateCodeAndKpiCode(String templateCode, String kpiCode);
    void deleteByRuleCodeIn(List<String> ruleCodes);
}
