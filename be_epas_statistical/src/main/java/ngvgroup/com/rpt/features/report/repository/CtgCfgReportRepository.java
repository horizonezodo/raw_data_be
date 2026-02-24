package ngvgroup.com.rpt.features.report.repository;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDtoV1;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportTypeDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgReportRepository extends JpaRepository<CtgCfgReport, Long> {

    @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDtoV1 (" +
            "r.id, r.reportCode, r.reportGroupCode, r.reportCodeName, rg.reportGroupName, r.reportType, r.templateCode, r.reportSource,r.sortNumber) "
            +
            "FROM CtgCfgReport r " +
            "INNER JOIN CtgCfgReportGroup rg ON r.reportGroupCode = rg.reportGroupCode " +
            "WHERE (:groupCode IS NULL OR rg.reportGroupCode = :groupCode) " +
            "AND (:reportType IS NULL OR r.reportType = :reportType ) " +
            "AND (:keyword IS NULL OR (" +
            "LOWER(r.reportCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.reportCodeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(STR(r.sortNumber)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.reportGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(rg.reportGroupName) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
            "ORDER BY r.modifiedDate DESC ")
    Page<ReportDtoV1> searchAllReportByGroup(@Param("reportType") String reportType,
            @Param("groupCode") String groupCode,
            @Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
            SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDtoV1 (
                r.id, r.reportCode, r.reportGroupCode, r.reportCodeName, rg.reportGroupName, r.reportType, r.templateCode, r.reportSource,r.sortNumber
            )
            FROM CtgCfgReportGroup rg
            INNER JOIN CtgCfgReport r ON r.reportGroupCode = rg.reportGroupCode
            WHERE (:keyword IS NULL OR
                LOWER(r.reportCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(r.reportCodeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(r.reportGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(STR(r.sortNumber)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(rg.reportGroupName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND (
                r.reportType IN :reportTypes
                AND rg.reportGroupCode IN :groupCodes
            )
            ORDER BY r.modifiedDate DESC
            """)
    Page<ReportDtoV1> searchAllReportByGroupIn(@Param("reportTypes") List<String> reportTypes,
                                               @Param("groupCodes") List<String> groupCodes,
                                               @Param("keyword") String keyword, Pageable pageable);

        Optional<CtgCfgReport> findByReportCode(String reportCode);

    Optional<CtgCfgReport> findCtgCfgReportByReportGroupCode(String reportGroupCode);

    void deleteComCfgReportByReportCode(String reportCode);

        @Query("""
                        SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportTypeDTO(
                            cc.commonCode,
                            cc.commonName,
                            rg.reportGroupCode,
                            rg.reportGroupName,
                            rg.description,
                            rg.sortNumber
                        )
                        FROM ComCfgCommon cc
                        JOIN CtgCfgReport r ON r.reportType = cc.commonCode
                        JOIN CtgCfgReportGroup rg ON rg.reportGroupCode = r.reportGroupCode
                        GROUP BY
                            cc.commonCode,
                            cc.commonName,
                            rg.reportGroupCode,
                            rg.reportGroupName,
                            rg.description,
                            rg.sortNumber
                        ORDER BY rg.sortNumber ASC
                        """)
        List<ReportTypeDTO> findAllGroupNames();

        @Query("""
                        SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportTypeDTO(
                            r.reportType,
                            rg.reportGroupCode,
                            rg.reportGroupName,
                            rg.description,
                            rg.sortNumber
                        )
                        FROM CtgCfgReport r
                        JOIN CtgCfgReportGroup rg ON rg.reportGroupCode = r.reportGroupCode
                        GROUP BY
                            r.reportType,
                            rg.reportGroupCode,
                            rg.reportGroupName,
                            rg.description,
                            rg.sortNumber
                        ORDER BY rg.sortNumber ASC
                        """)
        List<ReportTypeDTO> listGroupNames();

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDto (" +
                        "r.id, r.reportCode, r.reportGroupCode, r.reportCodeName, rg.reportGroupName, r.dataSourceType, r.templateCode, r.reportSource, r.sortNumber, rg.sortNumber) "
                        +
                        "FROM CtgCfgResourceMapping rm " +
                        "INNER JOIN CtgCfgReport r ON r.reportCode = rm.resourceCode AND rm.resourceTypeCode = 'CM032.003' "
                        +
                        "AND r.reportType = 'CM036.001' " +
                        "INNER JOIN CtgCfgReportGroup rg ON rg.reportGroupCode = r.reportGroupCode " +
                        "WHERE rm.userId = :userId " +
                        "ORDER BY rg.sortNumber ASC, r.sortNumber ASC")
        List<ReportDto> findAllReport(@Param("userId") String userId);

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDto (" +
                        "r.id, r.reportCode, r.reportGroupCode, r.reportCodeName, rg.reportGroupName, r.reportType, r.templateCode, r.reportSource, r.sortNumber, rg.sortNumber) "
                        +
                        "FROM CtgCfgReportGroup rg " +
                        "INNER JOIN CtgCfgReport r ON r.reportGroupCode = rg.reportGroupCode " +
                        "WHERE (:groupCode IS NULL OR rg.reportGroupCode = :groupCode ) " +
                        "ORDER BY rg.sortNumber ASC, r.sortNumber ASC")
        List<ReportDto> searchToExportExcel(@Param("groupCode") String groupCode);

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreport.ReportDto (" +
                        "r.id, r.reportCode, r.reportGroupCode, r.reportCodeName, r.reportCodeName, r.reportType, r.templateCode, r.reportSource, r.sortNumber, r.sortNumber) "
                        +
                        "FROM CtgCfgReport r " +
                        "WHERE r.reportType = :reportType " +
                        "ORDER BY r.sortNumber ASC")
        Page<ReportDto> findByReportType(@Param("reportType") String reportType, Pageable pageable);
}