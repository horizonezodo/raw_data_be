package com.naas.admin_service.features.log.repository;

import com.naas.admin_service.features.log.dto.ComInfLogAuditDto;
import com.naas.admin_service.features.log.dto.LogAuditSearchParams;
import com.naas.admin_service.features.log.model.ComInfLogAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ComInfLogAuditRepository extends JpaRepository<ComInfLogAudit, Long> {

    @Query("""
        SELECT new com.naas.admin_service.features.log.dto.ComInfLogAuditDto(
            t.auditId,
            t.tableName,
            t.fieldName,
            tt.username,
            tt.eventTime,
            tt.clientIp,
            tt.browserInfo,
            t.oldValue,
            t.newValue,
            t.requestId
        ) FROM ComInfLogAudit t
        LEFT JOIN ComInfLogActivity tt ON t.requestId = tt.requestId
        WHERE
        (:#{#params.browserInfo} IS NULL OR tt.browserInfo = :#{#params.browserInfo}) AND
        (:#{#params.username} IS NULL OR tt.username = :#{#params.username}) AND
        (:#{#params.clientIp} IS NULL OR tt.clientIp = :#{#params.clientIp}) AND
        (:#{#params.tableName} IS NULL OR t.tableName = :#{#params.tableName}) AND
        (:#{#params.fromDate} IS NULL OR tt.eventTime >= :#{#params.fromDate}) AND
        (:#{#params.toDate} IS NULL OR tt.eventTime < :#{#params.toDate}) AND
        ( :#{#params.keyword} IS NULL OR 
            LOWER(tt.username) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')) OR
            LOWER(t.tableName) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')) OR
            LOWER(t.fieldName) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')) OR
            LOWER(tt.clientIp) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%')) OR
            LOWER(tt.browserInfo) LIKE LOWER(CONCAT('%', :#{#params.keyword}, '%'))
        )
    """)
    Page<ComInfLogAuditDto> search(@Param("params") LogAuditSearchParams params, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ComInfLogAudit a WHERE a.requestId IN :requestIds")
    int deleteByRequestIdIn(List<String> requestIds);

    @Query("""
        SELECT new com.naas.admin_service.features.log.dto.ComInfLogAuditDto(
            t.auditId,
            t.tableName,
            t.fieldName,
            tt.username,
            tt.eventTime,
            tt.clientIp,
            tt.browserInfo,
            t.oldValue,
            t.newValue,
            t.requestId
        ) FROM ComInfLogAudit t
        LEFT JOIN ComInfLogActivity tt ON t.requestId = tt.requestId
        WHERE
            (:browserInfo IS NULL OR tt.browserInfo = :browserInfo) AND
            (:username IS NULL OR tt.username = :username) AND
            (:clientIp IS NULL OR tt.clientIp = :clientIp) AND
            (:tableName IS NULL OR t.tableName = :tableName) AND
            (:fromDate IS NULL OR tt.eventTime >= :fromDate) AND
            (:toDate IS NULL OR tt.eventTime <= :toDate) AND
            ( :keyword IS NULL OR 
                LOWER(tt.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(t.tableName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(t.fieldName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(tt.clientIp) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(tt.browserInfo) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    List<ComInfLogAuditDto> getAll(@Param("fromDate") Date fromDate,
                                   @Param("toDate") Date toDate,
                                   @Param("browserInfo") String browserInfo,
                                   @Param("username") String username,
                                   @Param("clientIp") String clientIp,
                                   @Param("tableName") String tableName,
                                   @Param("keyword") String keyword
                                );

    @Query("""
        SELECT new com.naas.admin_service.features.log.dto.ComInfLogAuditDto(
            t.auditId,
            t.fieldName,
            t.oldValue,
            t.newValue
        ) FROM ComInfLogAudit t
        LEFT JOIN ComInfLogActivity tt ON t.requestId = tt.requestId
        WHERE t.requestId = :requestId AND t.tableName = :tableName AND
        ( :keyword IS NULL OR 
            LOWER(t.fieldName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.oldValue) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.newValue) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<ComInfLogAuditDto> getAllByRequestIdAndTableName(@Param("requestId") String requestId, @Param("tableName") String tableName, @Param("keyword") String keyword, Pageable pageable);
}
