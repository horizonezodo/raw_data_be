package ngvgroup.com.bpm.features.sla.repository;

import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaProcess;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgSlaProcessRepository extends JpaRepository<ComCfgSlaProcess, Long> {
    @Query("""
                SELECT
                    com.orgCode              AS orgCode,
                    cfgSlaProcess.processTypeCode AS processTypeCode,
                    com.orgName              AS orgName,
                    cfgProcessType.processTypeName AS processTypeName,
                    cfgSlaProcess.slaType    AS slaType,
                    cfgSlaProcessDtl.slaWarningType AS slaWarningType,
                    cfgSlaProcessDtl.slaMaxDuration AS slaMaxDuration,
                    cfgSlaProcess.unit       AS unit,
                    cfgSlaProcessDtl.isActive AS isActive,
                    cfgSlaProcess.processDefineCode AS processDefineCode
                FROM ComCfgSlaProcess cfgSlaProcess
                JOIN ComInfOrganization com
                    ON cfgSlaProcess.orgCode = com.orgCode
                JOIN ComCfgProcessType cfgProcessType
                    ON cfgSlaProcess.processTypeCode = cfgProcessType.processTypeCode
                JOIN ComCfgSlaProcessDtl cfgSlaProcessDtl
                    ON cfgSlaProcess.processDefineCode = cfgSlaProcessDtl.processDefineCode
                   AND cfgSlaProcess.orgCode = cfgSlaProcessDtl.orgCode
            """)
    Page<ComCfgSlaDto.ComCfgSlaView> getListSla(Pageable pageable);

    @Query("""
                SELECT
                    com.orgCode              AS orgCode,
                    cfgSlaProcess.processTypeCode AS processTypeCode,
                    com.orgName              AS orgName,
                    cfgProcessType.processTypeName AS processTypeName,
                    cfgSlaProcess.slaType    AS slaType,
                    cfgSlaProcessDtl.slaWarningType AS slaWarningType,
                    cfgSlaProcessDtl.slaMaxDuration AS slaMaxDuration,
                    cfgSlaProcess.unit       AS unit,
                    cfgSlaProcessDtl.isActive AS isActive,
                    cfgSlaProcess.processDefineCode AS processDefineCode
                FROM ComCfgSlaProcess cfgSlaProcess
                JOIN ComInfOrganization com
                    ON cfgSlaProcess.orgCode = com.orgCode
                JOIN ComCfgProcessType cfgProcessType
                    ON cfgSlaProcess.processTypeCode = cfgProcessType.processTypeCode
                JOIN ComCfgSlaProcessDtl cfgSlaProcessDtl
                    ON cfgSlaProcess.processDefineCode = cfgSlaProcessDtl.processDefineCode
                   AND cfgSlaProcess.orgCode = cfgSlaProcessDtl.orgCode
                WHERE (
                    :keyword IS NULL OR :keyword = '' OR
                    LOWER(com.orgName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(cfgProcessType.processTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(cfgSlaProcess.slaType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    (LOWER('tự động') LIKE CONCAT('%', LOWER(:keyword), '%')
                        AND cfgSlaProcess.slaType = 'Auto') OR
                    (LOWER('thủ công') LIKE CONCAT('%', LOWER(:keyword), '%')
                        AND cfgSlaProcess.slaType = 'Manual') OR
                    (LOWER('cố định') LIKE CONCAT('%', LOWER(:keyword), '%')
                        AND cfgSlaProcessDtl.slaWarningType = 'FIXED') OR
                    (LOWER('phần trăm') LIKE CONCAT('%', LOWER(:keyword), '%')
                        AND cfgSlaProcessDtl.slaWarningType = 'PERCENT') OR
                    (LOWER('ngừng hoạt động') LIKE CONCAT('%', LOWER(:keyword), '%')
                        AND cfgSlaProcessDtl.isActive = 0) OR
                    (LOWER('đang hoạt động') LIKE CONCAT('%', LOWER(:keyword), '%')
                        AND cfgSlaProcessDtl.isActive = 1) OR
                    LOWER(cfgSlaProcessDtl.slaWarningType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    STR(cfgSlaProcessDtl.slaMaxDuration) LIKE CONCAT('%', :keyword, '%') OR
                    LOWER(cfgSlaProcess.unit) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    Page<ComCfgSlaDto.ComCfgSlaView> findSlaByKeyword(@Param("keyword") String keyword, Pageable pageable);


    @Query("""
                SELECT
                    com.orgCode              AS orgCode,
                    cfgSlaProcess.processTypeCode AS processTypeCode,
                    com.orgName              AS orgName,
                    cfgProcessType.processTypeName AS processTypeName,
                    cfgSlaProcess.slaType    AS slaType,
                    cfgSlaProcessDtl.slaWarningType AS slaWarningType,
                    cfgSlaProcessDtl.slaMaxDuration AS slaMaxDuration,
                    cfgSlaProcess.unit       AS unit,
                    cfgSlaProcessDtl.isActive AS isActive,
                    cfgSlaProcess.processDefineCode AS processDefineCode
                FROM ComCfgSlaProcess cfgSlaProcess
                JOIN ComInfOrganization com
                    ON cfgSlaProcess.orgCode = com.orgCode
                JOIN ComCfgProcessType cfgProcessType
                    ON cfgSlaProcess.processTypeCode = cfgProcessType.processTypeCode
                JOIN ComCfgSlaProcessDtl cfgSlaProcessDtl
                    ON cfgSlaProcess.processDefineCode = cfgSlaProcessDtl.processDefineCode
                   AND cfgSlaProcess.orgCode = cfgSlaProcessDtl.orgCode
                WHERE (
                    :keyword IS NULL OR :keyword = '' OR
                    LOWER(com.orgName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(cfgProcessType.processTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(cfgSlaProcess.slaType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(cfgSlaProcess.unit) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    List<ComCfgSlaDto.ComCfgSlaView> exportToExcel(@Param("keyword") String keyword);


    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSlaProcess c " +
            "SET  " +
            "    c.slaType = :slaType, " +
            "    c.unit = :unit, " +
            "    c.isActive = :isActive," +
            "c.modifiedDate = CURRENT_TIMESTAMP " +
            "WHERE c.processTypeCode = :processTypeCode " +
            "AND c.orgCode=:orgCode " +
            "AND c.isDelete=0")
    void updateSlaProcess(@Param("processTypeCode") String processTypeCode, @Param("orgCode") String orgCode, @Param("isActive") Integer isActive,
                          @Param("unit") String unit, @Param("slaType") String slaType);


    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto(" +
            "c.orgCode,c.processTypeCode,c.slaType,c.unit,c.isActive,c.processDefineCode)" +
            "FROM ComCfgSlaProcess c " +
            "WHERE c.processTypeCode=:processTypeCode " +
            "AND c.orgCode=:orgCode " +
            "AND c.isDelete=0")
    Optional<ComCfgSlaProcessDto> findInfComCfgSlaProcess(@Param("processTypeCode") String processTypeCode, @Param("orgCode") String orgCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM ComCfgSlaProcess c WHERE c.orgCode = :orgCode AND c.processTypeCode = :processTypeCode")
    void deleteComCfgSlaProcessByOrgCodeAndProcessTypeCode(@Param("orgCode") String orgCode, @Param("processTypeCode") String processTypeCode);

    @Query("""
        select new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto(
            s.orgCode,
            s.processTypeCode,
            s.slaType,
            s.unit,
            s.isActive,
            s.processDefineCode
        )
        from ComCfgSlaProcess s
        where s.orgCode = :orgCode
          and s.processTypeCode = :processTypeCode
          and s.isDelete = 0
          and s.isActive = 1
    """)
    Optional<ComCfgSlaProcessDto> findSlaProcess(@Param("orgCode") String orgCode,
                                                 @Param("processTypeCode") String processTypeCode);

    @Query("""
        select new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto(
            d.slaWarningType,
            d.slaMaxDuration,
            d.slaWarningPercent,
            d.slaWarningDuration,
            d.effectiveDate,
            d.processDefineCode,
            d.orgCode
        )
        from ComCfgSlaProcessDtl d
        where d.orgCode = :orgCode
          and d.processDefineCode = :processDefineCode
          and d.isDelete = 0
          and d.isActive = 1
        order by d.effectiveDate desc
    """)
    List<ComCfgSlaProcessDtlDto> findSlaDtlList(@Param("orgCode") String orgCode,
                                                @Param("processDefineCode") String processDefineCode);

    default Optional<ComCfgSlaProcessDtlDto> findLatestSlaDtl(String orgCode, String processDefineCode) {
        List<ComCfgSlaProcessDtlDto> list = findSlaDtlList(orgCode, processDefineCode);
        return (list == null || list.isEmpty()) ? Optional.empty() : Optional.of(list.get(0));
    }
}