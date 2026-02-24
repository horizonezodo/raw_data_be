package ngvgroup.com.rpt.features.transactionreport.repository;

import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportResponseV1Dto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportResultResponseDto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportSearchParams;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RptTxnStatTemplateRepository extends JpaRepository<RptTxnStatTemplate, Long> {


    Boolean existsByOrgCodeAndTemplateCodeAndReportPeriodAndReportDataDateAndIsVoid(
            String orgCode, String templateCode, String reportPeriod, Date reportDataDate, Integer isVoid);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportResponseV1Dto(
            t.id,
            t.currentStatusName,
            t.currentStatusCode,
            t.txnDate,
            t.statInstanceCode,
            t.templateCode,
            t.templateName,
            ct.templateGroupCode,
            ct.templateGroupName,
            c.commonName,
            t.reportDataDate,
            t.reportDueTime,
            ct.circularName,
            t.sendCount,
            t.exportCount,
            t.workflowCode,
            t.reportPeriod,
            ct.regulatoryTypeCode,
            ct.circularCode,
            t.orgCode,
            t.slaDueAt,
            t.slaElapsedTime,
            t.aggregationRunNo
        )
        FROM RptTxnStatTemplate t
        LEFT JOIN ComCfgCommon c ON c.commonCode = t.reportPeriod
        LEFT JOIN CtgCfgStatTemplate ct ON ct.templateCode = t.templateCode
        LEFT JOIN CtgCfgStatus s ON s.statusCode = t.currentStatusCode
        WHERE (:#{#params.orgCode} IS NULL OR t.orgCode = :#{#params.orgCode})
          AND (:#{#params.regulatoryTypeCode} IS NULL OR ct.regulatoryTypeCode = :#{#params.regulatoryTypeCode})
          AND (:#{#params.reportPeriod} IS NULL OR t.reportPeriod = :#{#params.reportPeriod})
          AND (:#{#params.statusCodes} IS NULL OR t.currentStatusCode IN :#{#params.statusCodes})
          AND (:#{#params.statInstanceCode} IS NULL OR t.statInstanceCode LIKE CONCAT('%', :#{#params.statInstanceCode}, '%'))
          AND (:#{#params.templateCode} IS NULL OR t.templateCode LIKE CONCAT('%', :#{#params.templateCode}, '%'))
          AND (:#{#params.fromDate} IS NULL OR t.txnDate >= :#{#params.fromDate})
          AND (:#{#params.toDate} IS NULL OR t.txnDate < :#{#params.toDate})
          AND (:#{#params.templateGroupCodes} IS NULL OR ct.templateGroupCode IN :#{#params.templateGroupCodes})
          AND (:#{#params.circularCodes} IS NULL OR ct.circularCode IN :#{#params.circularCodes})
          AND (:#{#params.keyword} IS NULL OR
                   LOWER(t.templateCode) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')) OR
                   LOWER(t.templateName) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')))
          AND (:#{#params.defaultCircularCodes} IS NULL OR ct.circularCode IN :#{#params.defaultCircularCodes})
              ORDER BY t.txnDate DESC
    """)
    Page<TransactionReportResponseV1Dto> search(
            @Param("params")TransactionReportSearchParams params,
            Pageable pageable
    );

    @Query("""
    SELECT t.statInstanceCode
    FROM RptTxnStatTemplate t
    WHERE t.templateCode = :templateCode
      AND t.reportPeriod = :reportPeriod
      AND t.reportDataDate = :reportDataDate
      AND t.isDelete = 0
    ORDER BY t.createdDate DESC
""")
    List<String> findPrevStatInstanceCodeOnly(
            String templateCode, String reportPeriod, Date reportDataDate);


    Optional<RptTxnStatTemplate> findByStatInstanceCode(String statInstanceCode);

    RptTxnStatTemplate findFirstByTemplateCodeIn(List<String> templateCodes);

    @Query("""
        SELECT s.revNo
        FROM RptTxnStatTemplate s
        WHERE s.prevStatInstanceCode = :prevStatInstanceCode
          AND s.isDelete = 0
          AND s.isActive = 1
        """)
    Integer findRevNoByInstanceCode(@Param("prevStatInstanceCode") String prevStatInstanceCode);

    List<RptTxnStatTemplate> findByIdIn(List<Long> ids);

    @Query("""
    SELECT MAX(t.aggregationRunNo)
    FROM RptTxnStatTemplate t
    WHERE t.orgCode = :orgCode
      AND t.templateCode = :templateCode
      AND t.reportPeriod = :reportPeriod
      AND t.reportDataDate = :reportDataDate
""")
    Integer findLatestAggregationRunNo(
            @Param("orgCode") String orgCode,
            @Param("templateCode") String templateCode,
            @Param("reportPeriod") String reportPeriod,
            @Param("reportDataDate") Date reportDataDate
    );

    @Query("""
        SELECT new ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportResultResponseDto(
            t.id,
            t.txnDate,
            t.statInstanceCode,
            t.templateCode,
            t.templateName,
            ct.templateGroupCode,
            ct.templateGroupName,
            c.commonName,
            t.reportDataDate,
            t.reportDueTime,
            ct.circularName,
            t.sendCount,
            t.exportCount,
            t.workflowCode,
            t.reportPeriod,
            ct.regulatoryTypeCode,
            ct.circularCode,
            t.orgCode,
            t.aggregationRunNo,
            t.makerUserName
        )
        FROM RptTxnStatTemplate t
        LEFT JOIN ComCfgCommon c ON c.commonCode = t.reportPeriod
        LEFT JOIN CtgCfgStatTemplate ct ON ct.templateCode = t.templateCode
        LEFT JOIN CtgCfgStatus s ON s.statusCode = t.currentStatusCode
        WHERE (:#{#params.orgCode} IS NULL OR t.orgCode = :#{#params.orgCode})
          AND (:#{#params.regulatoryTypeCode} IS NULL OR ct.regulatoryTypeCode = :#{#params.regulatoryTypeCode})
          AND (:#{#params.reportPeriod} IS NULL OR t.reportPeriod = :#{#params.reportPeriod})
          AND (:#{#params.statusCodes} IS NULL OR t.currentStatusCode IN :#{#params.statusCodes})
          AND (:#{#params.statInstanceCode} IS NULL OR t.statInstanceCode LIKE CONCAT('%', :#{#params.statInstanceCode}, '%'))
          AND (:#{#params.templateCode} IS NULL OR t.templateCode LIKE CONCAT('%', :#{#params.templateCode}, '%'))
          AND (:#{#params.fromDate} IS NULL OR t.txnDate >= :#{#params.fromDate})
          AND (:#{#params.toDate} IS NULL OR t.txnDate < :#{#params.toDate})
          AND (:#{#params.templateGroupCodes} IS NULL OR ct.templateGroupCode IN :#{#params.templateGroupCodes})
          AND (:#{#params.circularCodes} IS NULL OR ct.circularCode IN :#{#params.circularCodes})
          AND (:#{#params.keyword} IS NULL OR
                   LOWER(t.templateCode) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')) OR
                   LOWER(t.templateName) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')))
          AND s.isFinal = 1
    """)
    Page<TransactionReportResultResponseDto> searchResult(
            @Param("params") TransactionReportSearchParams params,
            Pageable pageable);


    List<RptTxnStatTemplate> findAllByStatInstanceCodeIn(List<String> statInstanceCodes);
}

