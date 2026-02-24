package ngvgroup.com.bpm.core.base.repository;

import ngvgroup.com.bpm.core.base.dto.CommentDto;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskComment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BpmTxnTaskCommentRepository extends JpaRepository<BpmTxnTaskComment, Long> {

    @Query("""
                SELECT new ngvgroup.com.bpm.core.base.dto.CommentDto(
                    c.taskComments,
                    c.modifiedBy,
                    c.modifiedDate,
                    i.taskDefineName,
                    c.taskStatus
                )
                FROM BpmTxnTaskComment c
                LEFT JOIN BpmTxnTaskInbox i ON c.taskId = i.taskId AND c.processInstanceCode = i.processInstanceCode
                WHERE c.processInstanceCode = :instanceCode
                ORDER BY c.modifiedDate DESC
            """)
    List<CommentDto> findCommentsByInstance(@Param("instanceCode") String instanceCode);
    // chưa lấy theo HRM_INF_EMPLOYEE và USER_ENTITY

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);
}
