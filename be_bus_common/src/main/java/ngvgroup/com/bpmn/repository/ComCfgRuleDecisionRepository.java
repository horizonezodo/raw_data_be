package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.model.ComCfgRuleDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComCfgRuleDecisionRepository extends JpaRepository<ComCfgRuleDecision, Long> {
}
