package ngvgroup.com.rpt.features.ctgcfgworkflow.repository;

import ngvgroup.com.rpt.features.smrtxnscore.dto.NextStepDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkflowTransitionRepository extends JpaRepository<WorkflowTransition,Long> {
    Optional<WorkflowTransition> findByWorkflowCodeAndToStatusCodeAndFromStatusCode(String workflowCode, String toStatusCode , String fromStatusCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrtxnscore.dto.NextStepDto(
                    w.toStatusName,
                    w.toStatusCode,
                    w.transitionCode,
                    w.transitionName
                )
                FROM WorkflowTransition w
                WHERE w.workflowCode = :workflowCode AND w.fromStatusCode = :fromStatusCode
            """)
    List<NextStepDto> getByWorkflowCode(@Param("workflowCode") String workflowCode , @Param("fromStatusCode") String fromStatusCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrtxnscore.dto.NextStepDto(
                    w.toStatusName,
                    w.toStatusCode,
                    w.transitionCode,
                    w.transitionName
                )
                FROM WorkflowTransition w
                JOIN ComCfgResourceMapping r ON w.toStatusCode = r.resourceCode
                WHERE w.workflowCode = :workflowCode AND w.fromStatusCode = :fromStatusCode
                AND r.userId = :userId
                AND r.isActive = 1
            """)
    List<NextStepDto> getListNextStep(@Param("workflowCode") String workflowCode , @Param("fromStatusCode") String fromStatusCode, @Param("userId") String userId);

    @Query("""
    SELECT t
    FROM WorkflowTransition t
    WHERE t.workflowCode = :workflowCode
      AND t.fromStatusCode = (
           SELECT w.initialStatusCode 
           FROM CtgCfgWorkflow w
           WHERE w.workflowCode = :workflowCode
      )
    """)
    List<WorkflowTransition> findFirstTransition(@Param("workflowCode") String workflowCode);


}
