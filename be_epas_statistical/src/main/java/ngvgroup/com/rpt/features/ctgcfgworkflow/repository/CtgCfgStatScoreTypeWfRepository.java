package ngvgroup.com.rpt.features.ctgcfgworkflow.repository;

import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgStatScoreTypeWf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CtgCfgStatScoreTypeWfRepository extends JpaRepository<CtgCfgStatScoreTypeWf,Long> {

    Optional<CtgCfgStatScoreTypeWf> findByStatScoreTypeCode(String statScoreTypeCode);



}
