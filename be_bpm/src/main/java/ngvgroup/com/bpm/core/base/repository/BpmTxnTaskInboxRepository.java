package ngvgroup.com.bpm.core.base.repository;

import ngvgroup.com.bpm.core.base.dto.TrackingDto;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskInbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BpmTxnTaskInboxRepository extends JpaRepository<BpmTxnTaskInbox, Long> {

    @Query("""
                select t.approvedBy
                from BpmTxnTaskInbox t
                where t.processInstanceCode = :processInstanceCode
                  and t.approvedDate is not null
                  and t.isDelete = 0
                order by t.approvedDate desc
            """)
    List<String> findApprovedByList(@Param("processInstanceCode") String processInstanceCode);

    default Optional<String> findLatestApprovedBy(String processInstanceCode) {
        List<String> list = findApprovedByList(processInstanceCode);
        return (list == null || list.isEmpty()) ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    boolean existsByTaskId(String id);

    Optional<BpmTxnTaskInbox> findByTaskId(String taskId);

    @Query("""
                SELECT new ngvgroup.com.bpm.core.base.dto.TrackingDto(
                    i.taskDefineName,
                    i.businessStatus,
                    i.taskStartTime,
                    i.acceptedDate,
                    (CASE WHEN i.businessStatus = 'COMPLETE' THEN i.taskUpdateTime ELSE null END),
                    i.acceptedBy
                )
                FROM BpmTxnTaskInbox i
                WHERE i.processInstanceCode = :instanceCode
                ORDER BY i.taskStartTime DESC
            """)
    List<TrackingDto> findInboxByInstance(@Param("instanceCode") String instanceCode);
    // chưa lấy theo HRM_INF_EMPLOYEE

    Optional<BpmTxnTaskInbox> findTopByProcessInstanceCodeOrderByIdDesc(String processInstanceCode);

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);

    @Query("""
                SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
                FROM BpmTxnTaskInbox t
                WHERE t.taskId = :taskId
                  AND t.assignTo IS NOT NULL
                  AND LOCATE(CONCAT(',', :userName, ','), CONCAT(',', t.assignTo, ',')) > 0
            """)
    boolean claimable(@Param("taskId") String taskId, @Param("userName") String userName);
}
