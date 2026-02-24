package ngvgroup.com.bpm.features.sla.repository;

import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto;
import ngvgroup.com.bpm.features.sla.dto.UpdateSlaProcessDtlCmd;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaProcessDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ComCfgSlaProcessDtlRepository extends JpaRepository<ComCfgSlaProcessDtl, Long> {

    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto(" +
            "c.slaWarningType,c.slaMaxDuration," +
            "c.slaWarningPercent,c.slaWarningDuration," +
            "c.effectiveDate,c.processDefineCode,c.orgCode) " +
            "FROM ComCfgSlaProcessDtl c " +
            "WHERE c.processDefineCode=:processTypeCode " +
            "AND c.orgCode=:orgCode " +
            "AND c.isDelete=0")
    Optional<ComCfgSlaProcessDtlDto> findInfoProcessDtl(@Param("processTypeCode") String processTypeCode, @Param("orgCode") String orgCode);

    @Modifying
    @Transactional
    @Query("""
    UPDATE ComCfgSlaProcessDtl c
       SET c.slaWarningType     = :#{#cmd.slaWarningType},
           c.slaMaxDuration     = :#{#cmd.slaMaxDuration},
           c.slaWarningPercent  = :#{#cmd.slaWarningPercent},
           c.slaWarningDuration = :#{#cmd.slaWarningDuration},
           c.effectiveDate      = :#{#cmd.effectiveDate},
           c.modifiedDate       = CURRENT_TIMESTAMP,
           c.isActive           = :#{#cmd.isActive}
     WHERE c.orgCode            = :#{#cmd.orgCode}
       AND c.processDefineCode  = :#{#cmd.processDefineCode}
""")
    void updateSlaProcessDtl(@Param("cmd") UpdateSlaProcessDtlCmd cmd);

    @Modifying
    @Transactional
    @Query("DELETE FROM ComCfgSlaProcessDtl d WHERE d.orgCode = :orgCode AND d.processDefineCode = :processDefineCode")
    void deleteComCfgSlaProcessDtlByOrgCodeAndProcessDefineCode(@Param("orgCode") String orgCode, @Param("processDefineCode") String processDefineCode);

    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSlaProcessDtl c SET c.slaWarningPercent=:slaWarningPercent " +
            "WHERE c.processDefineCode=:processDefineCode AND c.orgCode=:orgCode")
    void updateSlaWarningPercent(@Param("processDefineCode") String processDefineCode, @Param("orgCode") String orgCode
            , @Param("slaWarningPercent") Double slaWarningPercent);
}