package ngvgroup.com.loan.feature.scoring_indc_mapp.repository;

import ngvgroup.com.loan.feature.scoring_indc_mapp.model.CtgCfgScoringIndcMapp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgScoringIndcMappRepository extends JpaRepository<CtgCfgScoringIndcMapp,Long> {
    List<CtgCfgScoringIndcMapp> findAllByScoringIndcGroupCode(String scoringIndcGroupCode);
    List<CtgCfgScoringIndcMapp> getCtgCfgScoringIndcMappsByScoringIndcGroupCode(@Param("scoringIndcGroupCode") String scoringIndcGroupCode);

    void deleteAllByScoringIndcGroupCode(String code);
}
