package com.naas.admin_service.features.log.repository;

import com.naas.admin_service.features.log.dto.ComInfLogActivityDto;
import com.naas.admin_service.features.log.model.ComInfLogActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface ComInfLogActivityRepository extends JpaRepository<ComInfLogActivity, Long> {

    @Query("""
    SELECT new com.naas.admin_service.features.log.dto.ComInfLogActivityDto(
        t.activityId,
        t.eventTime,
        t.username,
        t.functionCode,
        t.actionName,
        t.durationTime,
        t.clientIp,
        t.browserInfo,
        t.statusCode,
        t.requestPayload
    ) FROM ComInfLogActivity t WHERE
        (:browserInfo IS NULL OR t.browserInfo = :browserInfo) AND
        (:username IS NULL OR t.username = :username) AND
        (:clientIp IS NULL OR t.clientIp = :clientIp) AND
        (:fromDate IS NULL OR t.eventTime >= :fromDate) AND
        (:toDate IS NULL OR t.eventTime <= :toDate) AND
        ( :keyword IS NULL OR 
            LOWER(t.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.functionCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.actionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.clientIp) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.browserInfo) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<ComInfLogActivityDto> search(@Param("fromDate") Date fromDate,
                                      @Param("toDate") Date toDate,
                                      @Param("browserInfo") String browserInfo,
                                      @Param("username") String username,
                                      @Param("clientIp") String clientIp,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);

    @Modifying
    @Query("DELETE FROM ComInfLogActivity a WHERE a.eventTime < :cutoff")
    void deleteByEventTimeBefore(@Param("cutoff") Date cutoff);

    @Query("SELECT a.requestId FROM ComInfLogActivity a WHERE a.eventTime < :cutoff")
    List<String> findRequestIdByEventTimeBefore(@Param("cutoff") Date cutoff);

    @Query("""
    SELECT new com.naas.admin_service.features.log.dto.ComInfLogActivityDto(
        t.activityId,
        t.eventTime,
        t.username,
        t.functionCode,
        t.actionName,
        t.durationTime,
        t.clientIp,
        t.browserInfo,
        t.statusCode,
        t.requestPayload
    ) FROM ComInfLogActivity t WHERE
        (:browserInfo IS NULL OR t.browserInfo = :browserInfo) AND
        (:username IS NULL OR t.username = :username) AND
        (:clientIp IS NULL OR t.clientIp = :clientIp) AND
        (:fromDate IS NULL OR t.eventTime >= :fromDate) AND
        (:toDate IS NULL OR t.eventTime <= :toDate) AND
        ( :keyword IS NULL OR 
            LOWER(t.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.functionCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.actionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.clientIp) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(t.browserInfo) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    List<ComInfLogActivityDto> getAll(@Param("fromDate") Date fromDate,
                                      @Param("toDate") Date toDate,
                                      @Param("browserInfo") String browserInfo,
                                      @Param("username") String username,
                                      @Param("clientIp") String clientIp,
                                      @Param("keyword") String keyword
                                    );
}