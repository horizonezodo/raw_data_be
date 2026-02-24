package ngvgroup.com.rpt.features.ctgcfgworkflow.repository;

import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgWorkflowTransitionRepository extends JpaRepository<WorkflowTransition, Long> {
    List<WorkflowTransition> findByWorkflowCode(String workflowCode);

    WorkflowTransition findFirstByFromStatusCodeAndWorkflowCode(String from, String wfCode);

    // Methods with isDelete filter
    List<WorkflowTransition> findByWorkflowCodeAndIsDelete(String workflowCode, int isDelete);

    @Modifying
    @Query("DELETE FROM WorkflowTransition wt WHERE wt.workflowCode = :workflowCode")
    void deleteByWorkflowCode(@Param("workflowCode") String workflowCode);

    WorkflowTransition findFirstByWorkflowCodeAndTransitionCodeAndFromStatusCode(
            String workflowCode,
            String transitionCode,
            String fromStatusCode
    );

    Boolean existsByWorkflowCodeAndTransitionCodeAndIsDelete(String workflowCode, String transitionCode, int isDelete);

}
