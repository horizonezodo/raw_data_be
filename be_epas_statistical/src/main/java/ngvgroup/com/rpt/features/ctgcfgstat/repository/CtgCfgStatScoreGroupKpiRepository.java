package ngvgroup.com.rpt.features.ctgcfgstat.repository;

import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatScoreGroupKpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatScoreGroupKpiRepository extends JpaRepository<CtgCfgStatScoreGroupKpi,Long> {

    List<CtgCfgStatScoreGroupKpi> findAllByStatScoreGroupCode(String statScoreGroupCode);

}
