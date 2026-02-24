package ngvgroup.com.bpm.features.sla.repository;

import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaTaskDtl;
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
public interface ComCfgSlaTaskDtlRepository extends JpaRepository<ComCfgSlaTaskDtl, Long> {
    ComCfgSlaTaskDtl findByTaskDefineCodeAndOrgCode(String taskDefineCode, String orgCode);

    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto(" +
            "c.orgCode,c.taskDefineCode,c.processDefineCode,c.effectiveDate,c.slaMaxDuration,c.slaWarningDuration,c.slaWarningPercent) " +
            "FROM ComCfgSlaTaskDtl c " +
            "WHERE c.processDefineCode=:processDefineCode " +
            "AND c.orgCode=:orgCode")
    List<ComCfgSlaTaskDtlDto> getInfoTaskDtl(@Param("processDefineCode") String processDefineCode, @Param("orgCode") String orgCode);

    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSlaTaskDtl c SET c.slaMaxDuration = :slaMaxDuration, " +
            "c.slaWarningDuration = :slaWarningDuration, c.slaWarningPercent = :slaWarningPercent ,c.slaWarningType=:slaWarningType,c.modifiedDate = CURRENT_TIMESTAMP " +
            "WHERE c.orgCode = :orgCode AND c.taskDefineCode = :taskDefineCode")
    void updateTaskDtl(@Param("slaMaxDuration") Double slaMaxDuration,
                       @Param("slaWarningDuration") Double slaWarningDuration,
                       @Param("slaWarningPercent") Double slaWarningPercent,
                       @Param("orgCode") String orgCode,
                       @Param("taskDefineCode") String taskDefineCode,
                       @Param("slaWarningType") String slaWarningType
    );

    @Transactional
    void deleteComCfgSlaTaskDtlByOrgCodeAndProcessDefineCode(String orgCode, String processDefineCode);

    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto(" +
            "c.orgCode,c.taskDefineCode,c.processDefineCode,c.effectiveDate,c.slaMaxDuration,c.slaWarningDuration,c.slaWarningPercent) " +
            "FROM ComCfgSlaTaskDtl c " +
            "WHERE c.taskDefineCode=:taskDefineCode " +
            "AND c.orgCode=:orgCode " +
            "AND c.processDefineCode=:processDefineCode")
    Optional<ComCfgSlaTaskDtlDto> findComCfgSlaTaskDtl(@Param("taskDefineCode") String taskDefineCode, @Param("orgCode") String orgCode, @Param("processDefineCode") String processDefineCode);

    @Query(
            value = "SELECT SUM(c.SLA_MAX_DURATION * c.SLA_WARNING_PERCENT) / SUM(c.SLA_MAX_DURATION) " +
                    "FROM COM_CFG_SLA_TASK_DTL c",
            nativeQuery = true
    )
    Double slaWarningPercentAuto();

    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto( " +
            "c.orgCode,c.taskDefineCode,c.processDefineCode,c.effectiveDate,c.slaMaxDuration,c.slaWarningDuration,c.slaWarningPercent,ct.priorityLevel,c.slaWarningType) " +
            "FROM ComCfgSlaTaskDtl c " +
            "JOIN ComCfgSlaTask ct ON c.taskDefineCode=ct.taskDefineCode AND c.orgCode=ct.orgCode " +
            "WHERE c.processDefineCode=:processDefineCode " +
            "AND c.orgCode=:orgCode")
    Page<ComCfgSlaTaskDtlDto> getDetailTask(@Param("processDefineCode") String processDefineCode, @Param("orgCode") String orgCode, Pageable pageable);

    @Query("SELECT SUM(c.slaMaxDuration) " +
            "FROM ComCfgSlaTaskDtl c")
    Double getSlaMaxDurationAuto();

    @Query("SELECT SUM(c.slaWarningDuration) " +
            "FROM ComCfgSlaTaskDtl c")
    Double getSlaWarningDurationAuto();

    Optional<ComCfgSlaTaskDtl> findByOrgCodeAndTaskDefineCodeAndProcessDefineCode(String orgCode, String taskDefineCode, String processDefineCode);

}