package ngvgroup.com.bpm.core.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.bpm.core.base.dto.AuditDto;
import ngvgroup.com.bpm.core.base.model.AudTxnAudit;

public interface AudTxnAuditRepository extends BaseRepository<AudTxnAudit> {

    @Query("""
            SELECT new ngvgroup.com.bpm.core.base.dto.AuditDto(
                a.fieldName,
                a.fieldCode,
                a.oldValue,
                a.newValue
            ) FROM AudTxnAudit a
            WHERE a.processInstanceCode = :processInstanceCode
                """)
    List<AuditDto> findAuditByInstance(@Param("processInstanceCode") String processInstanceCode);

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);

}
