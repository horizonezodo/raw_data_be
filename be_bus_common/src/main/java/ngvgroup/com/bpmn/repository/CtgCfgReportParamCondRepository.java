package ngvgroup.com.bpmn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO;
import ngvgroup.com.bpmn.model.CtgCfgReportParamCond;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgReportParamCondRepository extends JpaRepository<CtgCfgReportParamCond, Long> {

    @Query("""
                SELECT DISTINCT new ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO(
                    rpc.id,
                    rp.reportCode,
                    rpc.sourceParamCode,
                    rp.parameterName,
                    rpc.sourceParamValue,
                    '',
                    rpc.targetParamCode,
                    rp2.parameterName,
                    rpc.conditionType,
                    rpc.expression,
                    rpc.sortNumber,
                    rpc.description
                )
                FROM CtgCfgReportParamCond rpc
                INNER JOIN CtgCfgReportParam rp ON rpc.sourceParamCode = rp.parameterCode AND rpc.reportCode = rp.reportCode
                LEFT JOIN CtgCfgReportParam rp2 ON rpc.targetParamCode = rp2.parameterCode AND rpc.reportCode = rp.reportCode
                WHERE (rpc.reportCode = :reportCode) AND
                (:keyword IS NULL OR
                       LOWER(rp.parameterCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                       LOWER(rpc.sourceParamCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                        LOWER(rp2.parameterName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                         LOWER(rpc.conditionType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                       LOWER(rp.parameterName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<ReportParamCondDTO> searchAll(@Param("reportCode") String reportCode,
            @Param("keyword") String keyword,
            Pageable pageable);

        @Query("SELECT rpc FROM CtgCfgReportParamCond rpc " +
                        "WHERE rpc.reportCode = :reportCode AND rpc.sourceParamCode = :sourceParamCode")
        List<CtgCfgReportParamCond> findAllByReportCodeAndSourceParamCode(@Param("reportCode") String reportCode,
                        @Param("sourceParamCode") String sourceParamCode);

        @Query("SELECT rpc FROM CtgCfgReportParamCond rpc " +
                        "WHERE rpc.reportCode = :reportCode " +
                        "AND rpc.sourceParamCode = :sourceParamCode " +
                        "AND ((:sourceParamValue IS NULL AND rpc.sourceParamValue IS NULL) " +
                        "     OR rpc.sourceParamValue = :sourceParamValue) " +
                        "AND rpc.targetParamCode = :targetParamCode")
        List<CtgCfgReportParamCond> existingRecords(
                        @Param("reportCode") String reportCode,
                        @Param("sourceParamCode") String sourceParamCode,
                        @Param("sourceParamValue") String sourceParamValue,
                        @Param("targetParamCode") String targetParamCode);

      List<CtgCfgReportParamCond> findCtgCfgReportParamCondBySourceParamCodeAndReportCode(String sourceParamCode, String reportCode);

      List<CtgCfgReportParamCond> findCtgCfgReportParamCondByTargetParamCodeAndReportCode(String targetParamCode, String reportCode);
}