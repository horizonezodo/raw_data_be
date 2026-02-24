package ngvgroup.com.rpt.features.ctgcfgworkflow.repository;

import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.ExportExcelData;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgWorkflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgWorkflowRepository extends JpaRepository<CtgCfgWorkflow, Long> {
        Optional<CtgCfgWorkflow> findByWorkflowCode(String workflowCode);

        boolean existsByWorkflowCode(String workflowCode);

        // Methods with isDelete filter
        boolean existsByWorkflowCodeAndIsDelete(String workflowCode, int isDelete);

        CtgCfgWorkflow findByWorkflowCodeAndIsDelete(String workflowCode, int isDelete);

        @Query("SELECT w FROM CtgCfgWorkflow w WHERE w.isDelete = 0 " +
                        "AND (:keyword IS NULL OR :keyword = '' OR " +
                        "LOWER(w.workflowCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(w.workflowName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        Page<CtgCfgWorkflow> pageWorkflowWithVersion(@Param("keyword") String keyword, Pageable pageable);

        @Query("SELECT new ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.ExportExcelData(" +
                        "w.workflowCode, w.workflowName, w.initialStatusCode, w.versionNo) " +
                        "FROM CtgCfgWorkflow w WHERE w.isDelete = 0 " +
                        "AND (:keyword IS NULL OR :keyword = '' OR " +
                        "LOWER(w.workflowCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(w.workflowName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        List<ExportExcelData> listWorkflowWithVersion(@Param("keyword") String keyword);



}