package ngvgroup.com.rpt.features.report.repository;

import ngvgroup.com.rpt.features.report.model.CtgCfgReportParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgReportParamRepository extends JpaRepository<CtgCfgReportParam, Long> {
        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto(" +
                "rp.id, rp.reportCode, rp.parameterCode, rp.parameterName, rp.parameterType, rp.controlType, rp.resourceSql) " +
                "FROM CtgCfgReportParam rp " +
                "WHERE (:reportCode IS NULL OR rp.reportCode = :reportCode) " +
                "AND ( :keyword IS NULL OR " +
                "LOWER(rp.parameterCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
                "LOWER(rp.parameterName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
                "LOWER(rp.controlType) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
                "LOWER(rp.resourceSql) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
                "(LOWER('đầu vào') LIKE CONCAT('%', LOWER(:keyword), '%') AND rp.parameterType = '1') OR " +
                "(LOWER('cấu hình') LIKE CONCAT('%', LOWER(:keyword), '%') AND rp.parameterType = '2')" +
                ")")
        Page<ReportParamDto> searchAllByReportCode(@Param("reportCode") String reportCode,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        Optional<CtgCfgReportParam> findByParameterCode(String parameterCode);

        Optional<CtgCfgReportParam> findCtgCfgReportParamByReportCodeAndParameterCode(String reportCode,String parameterCode);

        void deleteByParameterCode(String parameterCode);

        @Query("SELECT rp.resourceSql " +
                        "FROM CtgCfgReportParam rp " +
                        "WHERE rp.parameterCode = :parameterCode AND rp.reportCode = :reportCode")
        String findResourceSqlBySourceParameterCode(@Param("parameterCode") String parameterCode,
                        @Param("reportCode") String reportCode);

        @Query("SELECT rp FROM CtgCfgReportParam rp " +
                        "WHERE rp.reportCode = :reportCode")
        List<CtgCfgReportParam> findAllByReportCode(@Param("reportCode") String reportCode);

        @Query("SELECT COUNT(*) > 0 " +
                        "FROM CtgCfgReportParam " +
                        "WHERE parameterCode = :parameterCode AND reportCode = :reportCode")
        boolean existsParameterCode(@Param("parameterCode") String parameterCode,
                        @Param("reportCode") String reportCode);

        @Query("""
                            SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto(
                            rp.id,
                            rp.reportCode,
                            rp.parameterCode,
                            rp.parameterName,
                            rp.parameterType,
                            rp.controlType,
                            rp.resourceSql
                            )
                            FROM CtgCfgReportParam rp
                            INNER JOIN CtgCfgReport r ON r.reportCode = rp.reportCode
                            WHERE rp.reportCode = :reportCode
                        """)
        List<ReportParamDto> getAllResourceParamName(@Param("reportCode") String reportCode);

        @Query("""
                            SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto(
                            rp.id,
                            rp.reportCode,
                            rp.parameterCode,
                            rp.parameterName,
                            rp.parameterType,
                            rp.controlType,
                            rp.resourceSql
                            )
                            FROM CtgCfgReportParam rp
                            INNER JOIN CtgCfgReport r ON r.reportCode = rp.reportCode
                            LEFT JOIN CtgCfgReportParamCond rpc
                                   ON rpc.sourceParamCode = rp.parameterCode
                                   AND rpc.reportCode = rp.reportCode
                                WHERE rpc.id IS NULL AND rp.reportCode = :reportCode
                        """)
        List<ReportParamDto> getAllTargetParamNames(@Param("reportCode") String reportCode);
}