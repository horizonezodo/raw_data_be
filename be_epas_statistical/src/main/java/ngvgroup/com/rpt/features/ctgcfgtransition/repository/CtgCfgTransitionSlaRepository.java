package ngvgroup.com.rpt.features.ctgcfgtransition.repository;

import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionSla;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CtgCfgTransitionSlaRepository extends JpaRepository<CtgCfgTransitionSla, Long> {

    CtgCfgTransitionSla findByTransitionCode(String transitionCode);
}
