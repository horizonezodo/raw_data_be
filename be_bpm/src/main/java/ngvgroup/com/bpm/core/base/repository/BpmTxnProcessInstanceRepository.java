package ngvgroup.com.bpm.core.base.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.bpm.features.transaction.dto.*;
import ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance;
import ngvgroup.com.bpm.features.transaction.filter.TransactionFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BpmTxnProcessInstanceRepository extends BaseRepository<BpmTxnProcessInstance> {

    String QRY_SELECT = """
            SELECT new ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryResponseDto(
                bp.processInstanceCode,
                bp.createdDate,
                c.processTypeName,
                bp.txnContent,
                bp.businessStatus,
                bp.slaProcessDeadline,
                bp.modifiedDate,
                bp.createdBy,
                t.taskId,
                bp.slaResult,
                bp.slaWarningType,
                bp.slaWarningDuration,
                bp.slaWarningPercent,
                bp.slaMaxDuration,
                t.formKey
            )
            """;

    String QRY_FROM = """
            FROM BpmTxnProcessInstance bp
            LEFT JOIN ComCfgProcessType c ON bp.processTypeCode = c.processTypeCode
            LEFT JOIN BpmTxnTaskInbox t ON t.processInstanceCode = bp.processInstanceCode
                 AND t.modifiedDate = (SELECT MAX(t2.modifiedDate) FROM BpmTxnTaskInbox t2 WHERE t2.processInstanceCode = bp.processInstanceCode)
            """;

    String QRY_WHERE_COMMON = """
            WHERE
                ( :keyword IS NULL OR :keyword = '' OR
                  LOWER(bp.processInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                  LOWER(bp.txnContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
                AND ( :#{#dto.orgCode} IS NULL OR :#{#dto.orgCode} = '' OR bp.orgCode = :#{#dto.orgCode} )
                AND ( :#{#dto.fromDate} IS NULL OR bp.createdDate >= :#{#dto.fromDate} )
                AND ( :#{#dto.toDate} IS NULL OR bp.createdDate < :#{#dto.toDate} )
                AND ( :#{#dto.processTypeCodes} IS NULL OR bp.processTypeCode IN :#{#dto.processTypeCodes} )
                AND (
                    :#{#dto.businessStatus} IS NULL
                    OR :#{#dto.businessStatus} = ''
                    OR :#{#dto.businessStatus} = 'ALL'
                    OR bp.businessStatus = :#{#dto.businessStatus}
                )
            """;

    String LOGIC_OVER = """
            (
               (bp.businessStatus IN ('COMPLETE', 'CANCEL', 'REJECT', 'COMPLETED') AND bp.slaResult = 'BREACHED')
               OR (
                   bp.businessStatus NOT IN ('COMPLETE', 'CANCEL', 'REJECT', 'COMPLETED')
                   AND CURRENT_TIMESTAMP > (bp.createdDate + COALESCE(bp.slaMaxDuration, 0) MINUTE)
               )
            )
            """;

    String LOGIC_WITHIN = """
            (
                (bp.businessStatus IN ('COMPLETE', 'CANCEL', 'REJECT', 'COMPLETED') AND bp.slaResult = 'ACHIEVED')
                OR (
                    bp.businessStatus NOT IN ('COMPLETE', 'CANCEL', 'REJECT', 'COMPLETED')
                    AND CURRENT_TIMESTAMP <= (bp.createdDate + COALESCE(bp.slaMaxDuration, 0) MINUTE)
                    AND (bp.slaWarningType IS NULL OR bp.slaWarningType <> 'FIXED' OR CURRENT_TIMESTAMP < (bp.createdDate + COALESCE(bp.slaWarningDuration, 0) MINUTE))
                    AND (bp.slaWarningType IS NULL OR bp.slaWarningType <> 'PERCENT' OR CURRENT_TIMESTAMP < (bp.createdDate + (COALESCE(bp.slaMaxDuration, 0) * COALESCE(bp.slaWarningPercent, 0) / 100.0) MINUTE))
                )
            )
            """;

    String LOGIC_APPROACH = """
            (
                bp.businessStatus NOT IN ('COMPLETE', 'CANCEL', 'REJECT', 'COMPLETED')
                AND CURRENT_TIMESTAMP <= (bp.createdDate + COALESCE(bp.slaMaxDuration, 0) MINUTE)
                AND (
                    (bp.slaWarningType = 'FIXED' AND CURRENT_TIMESTAMP >= (bp.createdDate + COALESCE(bp.slaWarningDuration, 0) MINUTE))
                    OR (bp.slaWarningType = 'PERCENT' AND CURRENT_TIMESTAMP >= (bp.createdDate + (COALESCE(bp.slaMaxDuration, 0) * COALESCE(bp.slaWarningPercent, 0) / 100.0) MINUTE))
                )
            )
            """;

    @Query("""
                SELECT new ngvgroup.com.bpm.features.transaction.dto.TransactionDto(
                    bp.processInstanceCode,
                    bt.createdDate,
                    bt.taskDefineName,
                    bp.txnContent,
                    bt.acceptedDate,
                    bt.slaMaxDuration,
                    bt.slaTaskDeadline,
                    bt.prevActionBy,
                    bt.acceptedBy,
                    bt.taskUpdateTime,
                    bt.slaResult,
                    bt.slaWarningType,
                    bt.slaWarningDuration,
                    bt.slaWarningPercent,
                    bt.formKey,
                    bt.taskId,
                    CASE WHEN bt.acceptedBy IS NULL OR bt.acceptedBy = :#{#f.userName} THEN bt.formAction ELSE NULL END,
                    bt.orgName,
                    bt.pathComplete
                )
                FROM  BpmTxnTaskInbox bt
                 JOIN BpmTxnProcessInstance bp
                    ON bp.processInstanceCode = bt.processInstanceCode
                 JOIN ComCfgProcessType c ON bp.processTypeCode=c.processTypeCode AND (:#{#f.isAccounting} is null or c.isAccounting=:#{#f.isAccounting})
                WHERE
                    (
                        :keyword IS NULL OR :keyword = '' OR
                        LOWER(bp.processInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bp.txnContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.taskDefineName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.acceptedBy) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.prevActionBy) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.slaResult) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    )
                    AND (
                        :#{#f.orgCode} IS NULL OR :#{#f.orgCode} = '' OR
                        bp.orgCode = :#{#f.orgCode}
                    )
                    AND ( :#{#f.fromDate} IS NULL OR bt.createdDate >= :#{#f.fromDate} )
                    AND ( :#{#f.toDate}   IS NULL OR bt.createdDate < :#{#f.toDate} )
                    AND ( :#{#f.businessStatus} IS NULL OR bt.businessStatus <> :#{#f.businessStatus} )
                    AND ( :#{#f.processTypeCodes} IS NULL OR bp.processTypeCode IN :#{#f.processTypeCodes} )
                    AND (
                        bt.assignTo IS NULL
                        OR LOCATE(CONCAT(',', :#{#f.userName}, ','), CONCAT(',', bt.assignTo, ',')) > 0
                    )
                ORDER BY bt.createdDate DESC,
                         CASE WHEN bt.acceptedBy IS NULL THEN 0
                              WHEN bt.acceptedBy = :#{#f.userName} THEN 1
                              ELSE 2
                         END ASC
            """)
    Page<TransactionDto> getTaskInbox(@Param("keyword") String keyword, @Param("f") TransactionFilter f,
            Pageable pageable);

    @Query("""
                SELECT new ngvgroup.com.bpm.features.transaction.dto.TransactionInboxExcel(
                    bp.processInstanceCode,
                    bt.createdDate,
                    bt.taskDefineName,
                    bp.txnContent,
                    bt.acceptedDate,
                    bt.slaMaxDuration,
                    bt.slaTaskDeadline,
                    bt.prevActionBy,
                    bt.acceptedBy,
                    bt.slaResult,
                    bt.slaWarningType,
                    bt.slaWarningDuration,
                    bt.slaWarningPercent,
                    bt.orgName
                )
                FROM  BpmTxnTaskInbox bt
                 JOIN BpmTxnProcessInstance bp
                    ON bp.processInstanceCode = bt.processInstanceCode
                 JOIN ComCfgProcessType c ON bp.processTypeCode=c.processTypeCode AND (:#{#f.isAccounting} is null or c.isAccounting=:#{#f.isAccounting})
                WHERE
                    (
                        :#{#f.orgCode} IS NULL OR :#{#f.orgCode} = '' OR
                        bp.orgCode = :#{#f.orgCode}
                    )
                    AND ( :#{#f.fromDate} IS NULL OR bt.createdDate >= :#{#f.fromDate} )
                    AND ( :#{#f.toDate}   IS NULL OR bt.createdDate < :#{#f.toDate} )
                    AND ( :#{#f.processTypeCodes} IS NULL
                          OR bp.processTypeCode IN :#{#f.processTypeCodes} )
                    AND ( :#{#f.businessStatus} IS NULL OR bt.businessStatus <> :#{#f.businessStatus} )
                ORDER BY bt.createdDate DESC
            """)
    List<TransactionInboxExcel> exportToExcelInbox(@Param("f") TransactionFilter f);

    @Query("""
                SELECT new ngvgroup.com.bpm.features.transaction.dto.TransactionDto(
                    bp.processInstanceCode,
                    bt.createdDate,
                    bt.taskDefineName,
                    bp.txnContent,
                    bt.acceptedDate,
                    bt.slaMaxDuration,
                    bt.slaTaskDeadline,
                    bt.prevActionBy,
                    bt.acceptedBy,
                    bt.taskUpdateTime,
                    bt.slaResult,
                    bt.slaWarningType,
                    bt.slaWarningDuration,
                    bt.slaWarningPercent,
                    bt.formKey,
                    bt.taskId,
                    bt.formAction,
                    bt.orgName,
                    bt.pathComplete
                )
                FROM  BpmTxnTaskInbox bt
                 JOIN BpmTxnProcessInstance bp
                    ON bp.processInstanceCode = bt.processInstanceCode
                WHERE
                    (
                        :keyword IS NULL OR :keyword = '' OR
                        LOWER(bp.processInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bp.txnContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.taskDefineName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.acceptedBy) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(bt.prevActionBy) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    )
                    AND (
                        :#{#f.orgCode} IS NULL OR :#{#f.orgCode} = '' OR
                        bp.orgCode = :#{#f.orgCode}
                    )
                    AND (
                        :#{#f.slaResult} IS NULL OR :#{#f.slaResult} = '' OR
                        bt.slaResult = :#{#f.slaResult}
                    )
                    AND ( :#{#f.fromDate} IS NULL OR bt.createdDate >= :#{#f.fromDate} )
                    AND ( :#{#f.toDate}   IS NULL OR bt.createdDate < :#{#f.toDate} )
                    AND ( :#{#f.processTypeCodes} IS NULL OR bp.processTypeCode IN :#{#f.processTypeCodes} )
                    AND ( :#{#f.businessStatus} IS NULL OR bt.businessStatus = :#{#f.businessStatus} )
                    AND ( :#{#f.userName} IS NULL OR bt.acceptedBy = :#{#f.userName} )
                ORDER BY bt.modifiedDate DESC
            """)
    Page<TransactionDto> getTaskOutbox(@Param("keyword") String keyword, @Param("f") TransactionFilter f,
            Pageable pageable);

    @Query("""
                SELECT new ngvgroup.com.bpm.features.transaction.dto.TransactionOutboxExcel(
                    bp.processInstanceCode,
                    bt.createdDate,
                    bt.taskDefineName,
                    bp.txnContent,
                    bt.acceptedDate,
                    bt.slaTaskDeadline,
                    bt.prevActionBy,
                    bt.taskUpdateTime
                )
                FROM  BpmTxnTaskInbox bt
                 JOIN BpmTxnProcessInstance bp
                    ON bp.processInstanceCode = bt.processInstanceCode
                WHERE
                    (
                        :#{#f.orgCode} IS NULL OR :#{#f.orgCode} = '' OR
                        bp.orgCode = :#{#f.orgCode}
                    )
                    AND (
                        :#{#f.slaResult} IS NULL OR :#{#f.slaResult} = '' OR
                        bp.slaResult = :#{#f.slaResult}
                    )
                    AND ( :#{#f.fromDate} IS NULL OR bt.createdDate >= :#{#f.fromDate} )
                    AND ( :#{#f.toDate}   IS NULL OR bt.createdDate < :#{#f.toDate} )
                    AND ( :#{#f.processTypeCodes} IS NULL
                          OR bp.processTypeCode IN :#{#f.processTypeCodes} )
                    AND ( :#{#f.businessStatus} IS NULL OR bt.businessStatus = :#{#f.businessStatus} )
                ORDER BY bt.modifiedDate DESC
            """)
    List<TransactionOutboxExcel> exportToExcelTaskOutbox(@Param("f") TransactionFilter f);

    Optional<BpmTxnProcessInstance> findByProcessInstanceCode(String processInstanceCode);

    @Query(QRY_SELECT + QRY_FROM + QRY_WHERE_COMMON + " ORDER BY bp.createdDate DESC")
    Page<TransactionInquiryResponseDto> searchTransactionInquiry(
            @Param("keyword") String keyword,
            @Param("dto") TransactionInquiryRequestDto dto,
            Pageable pageable);

    @Query(QRY_SELECT + QRY_FROM + QRY_WHERE_COMMON + " AND " + LOGIC_OVER + " ORDER BY bp.createdDate DESC")
    Page<TransactionInquiryResponseDto> searchTransactionInquiryOver(
            @Param("keyword") String keyword,
            @Param("dto") TransactionInquiryRequestDto dto,
            Pageable pageable);

    @Query(QRY_SELECT + QRY_FROM + QRY_WHERE_COMMON + " AND " + LOGIC_WITHIN + " ORDER BY bp.createdDate DESC")
    Page<TransactionInquiryResponseDto> searchTransactionInquiryWithin(
            @Param("keyword") String keyword,
            @Param("dto") TransactionInquiryRequestDto dto,
            Pageable pageable);

    @Query(QRY_SELECT + QRY_FROM + QRY_WHERE_COMMON + " AND " + LOGIC_APPROACH + " ORDER BY bp.createdDate DESC")
    Page<TransactionInquiryResponseDto> searchTransactionInquiryApproach(
            @Param("keyword") String keyword,
            @Param("dto") TransactionInquiryRequestDto dto,
            Pageable pageable);

    @Query("""
            SELECT new ngvgroup.com.bpm.features.transaction.dto.CustomerTransactionHistoryDto(
                        pi.txnDate,
                        e.employeeName,
                        pi.processTypeName,
                        pi.txnContent
                )
            FROM BpmTxnProcessInstance pi
            LEFT JOIN HrmInfEmployee e ON e.username = pi.createdBy
            WHERE pi.customerCode = :customerCode
            AND pi.processTypeCode IN :processTypeCodes
            ORDER BY pi.modifiedDate DESC
            """)
    List<CustomerTransactionHistoryDto> findCustomerTransactionHistory(@Param("customerCode") String customerCode,
            @Param("processTypeCodes") List<String> processTypeCodes);

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);
}
