package ngvgroup.com.bpm.features.sla.repository;

import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgSlaTaskRepository extends JpaRepository<ComCfgSlaTask, Long> {
    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDto( " +
            "c.priorityLevel,c.orgCode,c.taskDefineCode,c.processDefineCode,c.unit) " +
            "FROM ComCfgSlaTask c " +
            "WHERE c.processDefineCode=:processDefineCode " +
            "AND c.orgCode=:orgCode")
    List<ComCfgSlaTaskDto> getInfoTask(@Param("processDefineCode") String processDefineCode, @Param("orgCode") String orgCode);

    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSlaTask c SET c.priorityLevel = :priorityLevel,c.unit=:unit,c.modifiedDate = CURRENT_TIMESTAMP " +
            "WHERE c.orgCode = :orgCode AND c.taskDefineCode = :taskDefineCode")
    void updateTask(@Param("priorityLevel") String priorityLevel,
                    @Param("orgCode") String orgCode,
                    @Param("taskDefineCode") String taskDefineCode,
                    @Param("unit") String unit);

    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDto( " +
            "c.priorityLevel,c.orgCode,c.taskDefineCode,c.processDefineCode,c.unit) " +
            "FROM ComCfgSlaTask c " +
            "WHERE c.taskDefineCode=:taskDefineCode " +
            "AND c.orgCode=:orgCode " +
            "AND c.processDefineCode=:processDefineCode")
    Optional<ComCfgSlaTaskDto> findComCfgSlaTask(@Param("taskDefineCode") String taskDefineCode, @Param("orgCode") String orgCode, @Param("processDefineCode") String processDefineCode);

    @Transactional
    void deleteComCfgSlaTaskByOrgCodeAndProcessDefineCode(String orgCode, String processDefineCode);
}