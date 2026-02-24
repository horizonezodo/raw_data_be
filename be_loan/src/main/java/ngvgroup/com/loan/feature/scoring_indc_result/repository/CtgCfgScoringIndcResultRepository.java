package ngvgroup.com.loan.feature.scoring_indc_result.repository;

import ngvgroup.com.loan.feature.scoring_indc_result.model.CtgCfgScoringIndcResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgScoringIndcResultRepository extends JpaRepository<CtgCfgScoringIndcResult,Long> {

    List<CtgCfgScoringIndcResult> findAllByIndicatorCodeOrderBySortNumberAsc(String indicatorCode);
}
